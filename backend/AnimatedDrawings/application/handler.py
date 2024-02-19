import json
import logging
from datetime import datetime

from celery import Task
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from application.config.database_config import DatabaseConfig
from application.model.failed_task import FailedTask

logger = logging.getLogger('error_task')

database_config = DatabaseConfig()
engine = create_engine(database_config.get_database_url())


class LogErrorsTask(Task):
    autoretry_for = (Exception,)
    max_retries = 3
    retry_backoff = True
    retry_backoff_max = 700
    retry_jitter = False

    def on_failure(self, exc, task_id, args, kwargs, einfo):
        logger.error('fail task %s', task_id)
        self.save_failed_task(exc, task_id, args, kwargs, einfo)
        super(LogErrorsTask, self).on_failure(exc, task_id, args, kwargs, einfo)

    def on_retry(self, exc, task_id, args, kwargs, einfo):
        logger.error('retrying task %s', task_id)
        super(LogErrorsTask, self).on_retry(exc, task_id, args, kwargs, einfo)

    def save_failed_task(self, exc, task_id, args, kwargs, traceback):
        """
        :type exc: Exception
        """
        task = FailedTask(celery_task_id=task_id,
                          full_name=self.name,
                          name=self.name.split('.')[-1],
                          exception_class=exc.__class__.__name__,
                          exception_msg=str(exc).strip(),
                          traceback=str(traceback).strip(),
                          args=json.dumps(list(args)),
                          kwargs=json.dumps(kwargs),
                          created_at=datetime.utcnow(),
                          updated_at=datetime.utcnow())

        with Session(engine) as session:
            session.add(task)
            session.commit()
