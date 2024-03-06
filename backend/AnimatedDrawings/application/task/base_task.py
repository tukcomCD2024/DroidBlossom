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
    notification_server_url = 'https://notification.archive-timecapsule.kro.kr/api/notification/capsule_skin/send'

    def on_failure(self, exc, task_id, args, kwargs, einfo):
        logger.error('태스크 처리 실패 %s', task_id)
        request_data = {
            'memberId': kwargs['input_data']['memberId'],
            'skinName': kwargs['input_data']['skinName'],
            'title': '캡슐 스킨 생성에 실패했습니다',
            'text': f"{kwargs['input_data']['skinName']}이 생성되지 않았습니다. 다시 한 번 시도해주세요!",
            'skinUrl': kwargs['filename'],
            'status': CapsuleSkinCreationStatus.SUCCESS_MAKE_CAPSULE_SKIN.value
        }

        try:
            r = requests.post(self.notification_server_url,
                      json=request_data,
                      verify=False,
                      timeout=5)
            r.raise_for_status()
        except requests.exceptions.HTTPError as ex:
            logger.error('알림 서버 동작 오류 request: %s, response: %s', ex.request, ex.response)

        super(LogErrorsTask, self).on_failure(exc, task_id, args, kwargs, einfo)

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        logger.error('태스크 재시도 %s', task_id)
        super(LogErrorsTask, self).on_retry(exc, task_id, args, kwargs, einfo)

    def on_success(self, retval, task_id, args, kwargs):
        logger.info('태스크 처리 성공 %s', task_id)
