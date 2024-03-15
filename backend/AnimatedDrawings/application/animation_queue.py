import json
import logging
import uuid
from _xxsubinterpreters import ChannelClosedError
from json.decoder import JSONDecodeError

import pika
from celery import chain
from pika.adapters.blocking_connection import BlockingChannel
from pika.spec import Basic, BasicProperties

from application.config.queue_config import QueueConfig
from application.model.motion import Motion
from application.model.retarget import Retarget
from application.task.make_animation import MakeAnimation
from application.task.save_capsule_skin import SaveCapsuleSkin
from application.task.send_notification import SendNotification

logger = logging.getLogger('animation_queue_controller')


class AnimationQueueController:
    def __init__(self):
        self.queue_config = QueueConfig()
        self.require_keys = ['memberId', 'memberName', 'skinName', 'imageUrl',
                             'retarget', 'motionName']
        self.celery_work_queue_name = 'makeAnimation.queue'
        self.celery_success_queue_name = 'saveCapsuleSkin.queue'
        self.celery_send_notification_queue_name = 'sendNotification.queue'
        self.make_animation_task = MakeAnimation()
        self.save_capsule_skin_task = SaveCapsuleSkin()
        self.send_notification_task = SendNotification()

    def run(self):
        # rabbitmq 채널 연결
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=self.queue_config.queue_host))
        channel = connection.channel()

        channel.queue_declare(queue=self.queue_config.queue_name, durable=True)

        channel.basic_consume(queue=self.queue_config.queue_name,
                              on_message_callback=self.callback,
                              auto_ack=False)

        try:
            channel.start_consuming()
        except ChannelClosedError as e:
            logger.info("커넥션 연결 오류")
            raise e
        finally:
            channel.close()

    def callback(
        self,
        channel: BlockingChannel,
        method: Basic.Deliver,
        header: BasicProperties,
        body: bytes,
    ) -> None:
        """
        queue에서 message consume 시 동작하는 callback
        celery worker한테 animation 생성 작업을 실행한다
        :param channel: 큐와 연결된 채널
        :param method: 메시지의 상태
        :param header: 기본 정보
        :param body: queue로부터 넘어온 데이터
        """
        logger.info('큐 메시지 처리 시작 %s', header.message_id)
        try:
            json_object = self.parse_json(body)

            filename = f"capsuleSkin/{json_object['memberId']}/{uuid.uuid4()}.gif"

            chain(
                self.make_animation_task.s(input_data=json_object,
                                           filename=filename)
                .set(queue=self.celery_work_queue_name),

                self.save_capsule_skin_task.s(input_data=json_object,
                                              filename=filename)
                .set(queue=self.celery_success_queue_name),

                self.send_notification_task.s(input_data=json_object,
                                              filename=filename)
                .set(queue=self.celery_send_notification_queue_name)
            ).apply_async(
                ignore_result=True
            )

            channel.basic_ack(delivery_tag=method.delivery_tag)
        except Exception as e:
            logger.exception('메시지 처리 오류', e)
            channel.basic_reject(delivery_tag=method.delivery_tag,
                                 requeue=False)

    def parse_json(self, body: bytes):
        """
        json bytes를 파싱해 dict로 반환하는 함수
        :param body: queue로부터 넘어온 json bytes
        :return: body에서 파싱된 dict

        :raises JSONDecodeError: 유효하지 않은 json 형태인 경우
        :raise TypeError: 잘못된 json 입력 타입인 경우
        """
        try:
            json_object = json.loads(body.decode(encoding='utf8'))
            json_object['memberId'] = str(json_object['memberId'])
            json_object['retarget'] = Retarget[json_object['retarget']].value
            json_object['motionName'] = Motion[json_object['motionName']].value

            for key in self.require_keys:
                if key not in json_object:
                    raise KeyError

            return json_object

        except (JSONDecodeError, KeyError, TypeError) as e:
            logger.exception('json 파싱 오류', e)
            raise e


if __name__ == '__main__':
    application = AnimationQueueController()
    application.run()
