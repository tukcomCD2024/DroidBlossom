import celery.signals
import requests
from celery import Task
from celery.utils.log import get_task_logger

from application.logging.logger_factory import LoggerFactory
from application.model.notification_status import NotificationStatus


class LogErrorsTask(Task):
    autoretry_for = (Exception,)
    max_retries = 3
    retry_backoff = True
    retry_backoff_max = 700
    retry_jitter = False
    notification_server_url = 'https://notification.archive-timecapsule.kro.kr/api/notification/capsule_skin/send'

    def __init__(self):
        self.task_logger = get_task_logger(__name__)

    @celery.signals.after_setup_task_logger.connect
    def on_after_setup_logger(logger, **kwargs):
        LoggerFactory.setup_logger(logger)

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

        try:
            r = requests.post(self.notification_server_url,
                              json=request_data,
                              verify=False,
                              timeout=5)
            r.raise_for_status()
        except requests.exceptions.HTTPError as ex:
            self.task_logger.exception('알림 서버 동작 오류 %s', task_id,
                                       exc_info=ex)

        super(LogErrorsTask, self).on_failure(exc, task_id, args, kwargs, einfo)

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        self.task_logger.exception('태스크 재시도 %s', task_id, exc_info=einfo)
        super(LogErrorsTask, self).on_retry(exc, task_id, args, kwargs, einfo)

    def on_success(self, retval, task_id, args, kwargs):
        self.task_logger.info('태스크 처리 성공 %s', task_id)
