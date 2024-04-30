import uuid
from ast import literal_eval

from celery import chain
from kombu import Exchange, Queue

from application.config.queue_config import QueueConfig
from application.logging.logger_factory import LoggerFactory
from application.model.motion import Motion
from application.model.retarget import Retarget
from application.task.tasks import create_animation, save_capsule_skin, \
    send_notification
from kombu_connection_pool import connection, connections


class AnimationQueueController:
    def __init__(self):
        self.queue_config = QueueConfig()
        self.require_keys = ['memberId', 'memberName', 'skinName', 'imageUrl',
                             'retarget', 'motionName']
        self.celery_work_queue_name = 'makeAnimation.queue'
        self.celery_success_queue_name = 'saveCapsuleSkin.queue'
        self.celery_send_notification_queue_name = 'sendNotification.queue'
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
                               accept=['json']):
                self.logger.info('메시지 수신 시작')
                while True:
                    conn.drain_events()

    def callback(
        self,
        body: [dict, str, bytes],
        message: any
    ) -> None:
        """
        queue에서 message consume 시 동작하는 callback
        celery worker한테 animation 생성 작업을 실행한다
        """
        try:
            self.logger.debug('메시지 수신 완료, 콜백 동작')
            parsed_data = self.parse_body(body)

            filename = f"capsuleSkin/{parsed_data['memberId']}/{uuid.uuid4()}.gif"

            chain(
                create_animation.s(input_data=parsed_data,
                                   filename=filename)
                .set(queue=self.celery_work_queue_name),

                save_capsule_skin.s(input_data=parsed_data,
                                    filename=filename)
                .set(queue=self.celery_success_queue_name),

                send_notification.s(input_data=parsed_data,
                                    filename=filename)
                .set(queue=self.celery_send_notification_queue_name)
            ).apply_async(
                ignore_result=True
            )

            message.ack()
            self.logger.debug('celery에 작업 전달 완료')
        except Exception as e:
            self.logger.exception('작업 큐 메시지 처리 오류 %r', e)
            message.reject()

    def parse_body(self, body: [dict, str, bytes]):
        try:
            if isinstance(body, str):
                dict_data = literal_eval(body)
            elif isinstance(body, dict):
                dict_data = body
            elif isinstance(body, bytes):
                dict_data = literal_eval(body.decode('utf-8'))
            else:
                self.logger.error('처리할 수 없는 타입 오류')
                raise TypeError('처리할 수 없는 타입입니다')

            dict_data['retarget'] = Retarget[dict_data['retarget']].value
            dict_data['motionName'] = Motion[dict_data['motionName']].value

            for key in self.require_keys:
                if key not in dict_data:
                    raise KeyError

            return dict_data

        except (KeyError, TypeError) as e:
            self.logger.exception('작업 큐 메시지 파싱 오류 %r', e)
            raise e


if __name__ == '__main__':
    application = AnimationQueueController()
    application.run()
