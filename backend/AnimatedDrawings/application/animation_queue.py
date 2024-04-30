import argparse
import json
import uuid
from json.decoder import JSONDecodeError

from celery import chain
from kombu import Exchange, Queue

from kombu_connection_pool import connection, connections

from application.config.queue_config import QueueConfig
from application.logging.logger_factory import LoggerFactory
from application.model.motion import Motion
from application.model.retarget import Retarget
from application.task.make_animation import MakeAnimation
from application.task.save_capsule_skin import SaveCapsuleSkin
from application.task.send_notification import SendNotification


class AnimationQueueController:
    def __init__(self, output_file_path: str):
        self.queue_config = QueueConfig()
        self.require_keys = ['memberId', 'memberName', 'skinName', 'imageUrl',
                             'retarget', 'motionName']
        self.celery_work_queue_name = 'makeAnimation.queue'
        self.celery_success_queue_name = 'saveCapsuleSkin.queue'
        self.celery_send_notification_queue_name = 'sendNotification.queue'
        self.make_animation_task = MakeAnimation()
        self.save_capsule_skin_task = SaveCapsuleSkin()
        self.send_notification_task = SendNotification()
        self.output_file_path = output_file_path
        self.logger = LoggerFactory.get_logger(__name__)

    def run(self):
        capsule_skin_exchange = Exchange(
            name=QueueConfig.CAPSULE_SKIN_REQUEST_EXCHANGE_NAME,
            type='direct',
            durable=True)
        capsule_skin_queue = Queue(
            name=QueueConfig.CAPSULE_SKIN_REQUEST_QUEUE_NAME,
            exchange=capsule_skin_exchange,
            routing_key=QueueConfig.CAPSULE_SKIN_REQUEST_QUEUE_NAME)

        with connections[connection].acquire(block=True) as conn:
            with conn.Consumer(queues=[capsule_skin_queue],
                               callbacks=[self.callback],
                               accept=['json']) as consumer:
                self.logger.info('메시지 수신 시작')
                while True:
                    conn.drain_events()

    def callback(
        self,
        body: str,
        message
    ) -> None:
        """
        queue에서 message consume 시 동작하는 callback
        celery worker한테 animation 생성 작업을 실행한다
        """
        try:
            self.logger.debug('메시지 수신 완료, 콜백 동작')
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

            message.ack()
            self.logger.debug('celery에 작업 전달 완료')
        except Exception as e:
            self.logger.exception('작업 큐 메시지 처리 오류', exc_info=e)
            message.reject()

    def parse_json(self, body: str):
        """
        json bytes를 파싱해 dict로 반환하는 함수
        :param body: queue로부터 넘어온 json bytes
        :return: body에서 파싱된 dict

        :raises JSONDecodeError: 유효하지 않은 json 형태인 경우
        :raise TypeError: 잘못된 json 입력 타입인 경우
        """
        try:
            json_object = json.loads(body)
            json_object['memberId'] = str(json_object['memberId'])
            json_object['retarget'] = Retarget[json_object['retarget']].value
            json_object['motionName'] = Motion[json_object['motionName']].value

            for key in self.require_keys:
                if key not in json_object:
                    raise KeyError

            return json_object

        except (JSONDecodeError, KeyError, TypeError) as e:
            self.logger.exception('작업 큐 메시지 json 파싱 오류', exc_info=e)
            raise e


def parse_args() -> str:
    parser = argparse.ArgumentParser()
    parser.add_argument("-o", "--output", help="log file path")
    args = parser.parse_args()

    if args.output:
        return args.output

    return ''


if __name__ == '__main__':
    output_log_path = parse_args()
    application = AnimationQueueController(output_log_path)
    application.run()
