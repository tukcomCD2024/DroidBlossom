import json

import celery.signals
from celery.utils.log import get_task_logger
from kombu import Exchange, Queue

from application.config.logger_config import LoggerConfig
from application.config.queue_config import QueueConfig
from application.kombu_connection_pool import producers, connection
from application.logging.logger_factory import LoggerFactory
from application.model.notification_status import NotificationStatus
from application.task.base_task import LogErrorsTask


class SendNotification(LogErrorsTask):
    name = 'send_notification'

    def __init__(self):
        self.task_logger = get_task_logger(__name__)

    @celery.signals.after_setup_task_logger.connect
    def on_after_setup_logger(logger, **kwargs):
        LoggerFactory.setup_logger(logger, LoggerConfig.CELERY_OUTPUT_FILE_PATH)

    def run(self, *args, **kwargs):
        self.task_logger.debug('알림 전송 작업 시작')
        request_data = json.dumps({
            'memberId': kwargs['input_data']['memberId'],
            'skinName': kwargs['input_data']['skinName'],
            'title': '캡슐 스킨 생성이 완료되었습니다',
            'text': f"{kwargs['input_data']['skinName']}이 생성되었습니다. ARchive에서 확인해보세요!",
            'skinUrl': kwargs['filename'],
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
                routing_key=QueueConfig.NOTIFICATION_QUEUE_NAME,
            )
        self.task_logger.info(args)
        self.task_logger.info(kwargs)
        self.task_logger.debug('알림 전송 작업 완료')
