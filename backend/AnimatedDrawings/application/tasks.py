import os.path
import shutil
from pathlib import Path

import requests
from celery import Celery
from kombu import Queue
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from application.config.database_config import DatabaseConfig
from application.config.queue_config import QueueConfig
from application.config.s3_config import S3Config
from application.handler import LogErrorsTask
from application.model.capsule_skin import CapsuleSkin
from application.model.motion import Motion
from application.model.retarget import Retarget
from application.s3.s3_connection import get_object_wrapper
from examples.annotations_to_animation import annotations_to_animation
from examples.image_to_annotations import image_to_annotations

queue_config = QueueConfig()
celery = Celery('tasks',
                broker=queue_config.get_queue_url(),
                include=['application.tasks'])

celery.conf.result_expires = 300
celery.conf.task_queues = (
    Queue('makeAnimationTask.queue'),
    Queue('saveCapsuleSkinTasks.queue'),
)

database_config = DatabaseConfig()
engine = create_engine(database_config.get_database_url())

s3_config = S3Config()


@celery.task(base=LogErrorsTask)
def make_animation(input_data: dict, filename: str) -> None:
    img_bytes = requests.get(input_data['imageUrl']).content

    output_directory = image_to_animation(img_bytes, input_data)

    upload_gif_to_s3(output_directory, filename)

    clear_resource(output_directory)


def image_to_animation(img_bytes: bytes, input_data: dict) -> str:
    output_directory = 'capsuleSkin/' + input_data['memberId']

    output_path = create_directory(output_directory)

    image_to_annotations(img_bytes, output_path)
    annotations_to_animation(output_directory, input_data['motionName'],
                             input_data['retarget'])
    return output_directory


def create_directory(output_directory: str) -> Path:
    result = Path(output_directory)
    result.mkdir(exist_ok=True)
    return result


def upload_gif_to_s3(output: str, filename: str) -> None:
    gif_bytes = read_animation_result(output + '/video.gif')

    output_wrapper = get_object_wrapper(s3_config.s3_bucket_name, output + filename)
    output_wrapper.put(gif_bytes)


def read_animation_result(output: str) -> bytes:
    with open(output, 'rb') as image:
        return bytearray(image.read())


def clear_resource(output_directory: str) -> None:
    if os.path.exists(output_directory):
        shutil.rmtree(output_directory)


@celery.task(base=LogErrorsTask)
def save_capsule_skin(_, input_data: dict, filename: str) -> None:
    capsule_skin = CapsuleSkin(skin_name=input_data['skinName'],
                               image_url='capsuleSkin/%s/%s' % (
                                   input_data['memberId'], filename),
                               motion_name=Motion(input_data['motionName']).name,
                               retarget=Retarget(input_data['retarget']).name,
                               member_id=input_data['memberId'])

    with Session(engine) as session:
        session.add(capsule_skin)
        session.commit()
