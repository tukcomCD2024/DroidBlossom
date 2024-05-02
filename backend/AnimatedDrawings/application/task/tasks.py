import json
import os
import shutil
import uuid
from pathlib import Path

import requests
from kombu import Exchange, Queue
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from application.celery_app import celery
from application.config.database_config import DatabaseConfig
from application.config.queue_config import QueueConfig
from application.config.s3_config import S3Config
from application.kombu_connection_pool import producers, connection
from application.model.capsule_skin import CapsuleSkin
from application.model.motion import Motion
from application.model.notification_status import NotificationStatus
from application.model.retarget import Retarget
from application.s3.s3_connection import get_object_wrapper
from application.task.base_task import LogErrorsTask
from examples.annotations_to_animation import annotations_to_animation
from examples.image_to_annotations import image_to_annotations

engine = create_engine(DatabaseConfig.get_database_url())
s3_bucket_name = S3Config.S3_BUCKET_NAME


@celery.task(base=LogErrorsTask)
def create_animation(input_data: dict, filename: str):
    """
    애니메이션 생성 task
    :param input_data: 입력 데이터(dict) - imageUrl, motionName, retarget, skinName, memberId, memberName
    :param filename: 원격지에 저장될 파일 이름 ex) capsuleSkin/2/1234.gif
    :return:
    """
    img_bytes = requests.get(input_data['imageUrl']).content

    temporary_directory = f'capsuleSkin/{uuid.uuid4()}'
    result = Path(temporary_directory)
    result.mkdir(exist_ok=True)

    image_to_annotations(img_bytes, result)
    annotations_to_animation(temporary_directory,
                             input_data['motionName'],
                             input_data['retarget'])

    with open(f'{temporary_directory}/video.gif', 'rb') as image:
        gif_bytes = bytearray(image.read())

    output_wrapper = get_object_wrapper(s3_bucket_name, filename)

    output_wrapper.put(gif_bytes)

    if os.path.exists(temporary_directory):
        shutil.rmtree(temporary_directory)


@celery.task(base=LogErrorsTask)
def save_capsule_skin(_, input_data: dict, filename: str):
    """
    캡슐 스킨 생성 정보 DB 저장 태스크
    :param _: 이전 task 결과
    :param input_data: 입력 데이터(dict) - imageUrl, motionName, retarget, skinName, memberId, memberName
    :param filename: 원격지에 저장될 파일 이름 ex) capsuleSkin/2/1234.gif
    :return:
    """
    capsule_skin = CapsuleSkin(skin_name=input_data['skinName'],
                               image_url=filename,
                               motion_name=Motion(
                                   input_data['motionName']).name,
                               retarget=Retarget(input_data['retarget']).name,
                               member_id=input_data['memberId'])

    with Session(engine) as session:
        session.add(capsule_skin)
        session.commit()


@celery.task(base=LogErrorsTask)
def send_notification(_, input_data: dict, filename: str):
    """
    캡슐 스킨 생성 완료 알림 전송 태스크
    :param _: 이전 task 결과
    :param input_data: 입력 데이터(dict) - imageUrl, motionName, retarget, skinName, memberId, memberName
    :param filename: 원격지에 저장될 파일 이름 ex) capsuleSkin/2/1234.gif
    :return:
    """
    request_data = json.dumps({
        'memberId': input_data['memberId'],
        'skinName': input_data['skinName'],
        'title': '캡슐 스킨 생성이 완료되었습니다',
        'text': f"{input_data['skinName']}이 생성되었습니다. ARchive에서 확인해보세요!",
        'skinUrl': filename,
        'status': NotificationStatus.SUCCESS.value
    }, ensure_ascii=False)

    with producers[connection].acquire(block=True) as producer:
        exchange = Exchange(name=QueueConfig.NOTIFICATION_EXCHANGE_NAME,
                            type='direct',
                            durable=True)

        queue = Queue(name=QueueConfig.NOTIFICATION_QUEUE_NAME,
                      exchange=exchange,
                      routing_key=QueueConfig.NOTIFICATION_QUEUE_NAME)

        producer.publish(
            request_data,
            declare=[queue],
            exchange=exchange,
            content_type='application/json',
            routing_key=QueueConfig.NOTIFICATION_QUEUE_NAME,
        )
