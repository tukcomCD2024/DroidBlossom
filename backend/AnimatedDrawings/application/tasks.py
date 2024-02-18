import pathlib
import shutil

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
from application.s3.s3_connection import get_object_wrapper
from examples.annotations_to_animation import annotations_to_animation
from examples.image_to_annotations import image_to_annotations

queue_config = QueueConfig()
celery = Celery('tasks',
                broker=queue_config.get_queue_url(),
                include=['tasks'])

celery.conf.result_expires = 300
celery.conf.task_queues = (
    Queue('q.animation_tasks'),
    Queue('q.success_tasks'),
)

database_config = DatabaseConfig()
engine = create_engine(database_config.get_database_url())

s3_config = S3Config()


@celery.task(base=LogErrorsTask)
def make_animation(input_data: dict) -> None:
    img_bytes = requests.get(input_data['imageUrl']).content

    output_directory = image_to_animation(img_bytes, input_data)

    upload_gif_to_s3(output_directory)

    clear_resource(output_directory)


def image_to_animation(img_bytes: bytes, input_data: dict) -> str:
    output_directory = "capsule_skin/" + input_data['memberId']
    image_to_annotations(img_bytes, output_directory)
    annotations_to_animation(output_directory, input_data['motionName'],
                             input_data['retarget'])
    return output_directory


def upload_gif_to_s3(output: str) -> None:
    result_path = output + "/video.gif"

    gif_bytes = read_animation_result(result_path)

    output_wrapper = get_object_wrapper(s3_config.s3_bucket_name(), result_path)
    output_wrapper.put(gif_bytes)


def read_animation_result(output: str) -> bytes:
    with open(output, "rb") as image:
        return bytearray(image.read())


def clear_resource(output_directory):
    if pathlib.Path.is_dir(output_directory):
        shutil.rmtree(output_directory)


@celery.task(base=LogErrorsTask)
def save_capsule_skin(_, input_data: dict) -> None:
    capsule_skin = CapsuleSkin(skin_name=input_data['skinName'],
                               image_url='/capsuleSkin/%s/%s' % (
                                   input_data['memberId'], 'video.gif'),
                               motion_name=input_data['motionName'],
                               retarget=input_data['retarget'],
                               member_id=input_data['memberId'])

    with Session(engine) as session:
        session.add(capsule_skin)
        session.commit()
