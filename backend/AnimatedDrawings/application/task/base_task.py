import logging

import requests
from celery import Task

from application.model.capsule_skin_creation_status import \
    CapsuleSkinCreationStatus

logger = logging.getLogger('error_task')


class LogErrorsTask(Task):
    autoretry_for = (Exception,)
    max_retries = 3
    retry_backoff = True
    retry_backoff_max = 700
    retry_jitter = False
    notification_server_url = 'http://localhost:8080/api/notification/capsule_skin/send'

    def on_failure(self, exc, task_id, args, kwargs, einfo):
        logger.error('태스크 처리 실패 %s', task_id)
        request_data = {
            'memberId': kwargs['input_data']['memberId'],
            'skinName': kwargs['input_data']['skinName'],
            'title': self.title,
            'text': f"{kwargs['input_data']['skinName']}이 생성되었습니다. ARchive에서 확인해보세요!",
            'skinUrl': kwargs['filename'],
            'status': CapsuleSkinCreationStatus.SUCCESS_MAKE_CAPSULE_SKIN.value
        }

        requests.post(self.notification_server_url,
                      json=request_data,
                      verify=False,
                      timeout=5)
        super(LogErrorsTask, self).on_failure(exc, task_id, args, kwargs, einfo)

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        logger.error('태스크 재시도 %s', task_id)
        super(LogErrorsTask, self).on_retry(exc, task_id, args, kwargs, einfo)

    def on_success(self, retval, task_id, args, kwargs):
        logger.info('태스크 처리 성공 %s', task_id)
