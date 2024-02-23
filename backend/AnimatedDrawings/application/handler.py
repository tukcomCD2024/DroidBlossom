import logging

from celery import Task

logger = logging.getLogger('error_task')


class LogErrorsTask(Task):
    autoretry_for = (Exception,)
    max_retries = 3
    retry_backoff = True
    retry_backoff_max = 700
    retry_jitter = False

    def on_failure(self, exc, task_id, args, kwargs, einfo):
        logger.error('태스크 처리 실패 %s', task_id)
        super(LogErrorsTask, self).on_failure(exc, task_id, args, kwargs, einfo)

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        logger.error('태스크 재시도 %s', task_id)
        super(LogErrorsTask, self).on_retry(exc, task_id, args, kwargs, einfo)

    def on_success(self, retval, task_id, args, kwargs):
        logger.info('태스크 처리 성공 %s', task_id)
