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
        logger.error('fail task %s', task_id)
        super(LogErrorsTask, self).on_failure(exc, task_id, args, kwargs, einfo)

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        logger.error('retrying task %s', task_id)
        super(LogErrorsTask, self).on_retry(exc, task_id, args, kwargs, einfo)
