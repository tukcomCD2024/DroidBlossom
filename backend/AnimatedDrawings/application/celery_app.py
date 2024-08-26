from celery import Celery
from kombu import Queue

from application.config.queue_config import QueueConfig

queue_config = QueueConfig()
celery = Celery('application.task',
                broker=queue_config.get_broker_url(),
                include=['application.task.tasks']
                )

celery.conf.result_expires = 300
celery.conf.task_queues = (Queue('task.makeAnimation.queue'),
                           Queue('task.saveCapsuleSkin.queue'),
                           Queue('task.sendNotification.queue')
                           )

if queue_config.PROTOCOL == 'amqps':
    import ssl
    celery.conf.broker_use_ssl = {
        'cert_reqs': ssl.CERT_REQUIRED
    }
