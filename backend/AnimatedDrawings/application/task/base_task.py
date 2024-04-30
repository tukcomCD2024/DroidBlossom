import celery.signals
from celery import Task
from celery.utils.log import get_task_logger
from kombu import Queue, Exchange

from application.kombu_connection_pool import producers, connection
from application.config.logger_config import LoggerConfig
from application.config.queue_config import QueueConfig
from application.logging.logger_factory import LoggerFactory
from application.model.notification_status import NotificationStatus


class LogErrorsTask(Task):
    autoretry_for = (Exception,)
    max_retries = 3
    retry_backoff = True
    retry_backoff_max = 700
    retry_jitter = False

    def __init__(self):
        self.task_logger = get_task_logger(__name__)

    @celery.signals.after_setup_task_logger.connect
    def on_after_setup_logger(logger, **kwargs):
        LoggerFactory.setup_logger(logger, LoggerConfig.CELERY_OUTPUT_FILE_PATH)

    def before_start(self, task_id, args, kwargs):
        self.task_logger.debug(kwargs)
        self.task_logger.debug('태스크 처리 시작 %s', task_id)

    def on_failure(self, exc, task_id, args, kwargs, einfo):
        self.task_logger.exception('태스크 처리 실패 %s', task_id, exc_info=einfo)
        request_data = {
            'memberId': kwargs['input_data']['memberId'],
            'skinName': kwargs['input_data']['skinName'],
            'title': '캡슐 스킨 생성에 실패했습니다',
            'text': f"{kwargs['input_data']['skinName']}이 생성되지 않았습니다. 다시 한 번 시도해주세요!",
            'skinUrl': kwargs['filename'],
            'status': NotificationStatus.SUCCESS.value
        }
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

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        self.task_logger.debug(kwargs)
        self.task_logger.exception('태스크 재시도 %s', task_id, exc_info=einfo)

    def on_success(self, retval, task_id, args, kwargs):
        self.task_logger.debug(args)

        self.task_logger.debug('태스크 처리 성공 %s', task_id)
