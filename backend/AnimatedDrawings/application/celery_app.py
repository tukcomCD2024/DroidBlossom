from celery import Celery
from kombu import Queue

from application.config.queue_config import QueueConfig
from application.task.make_animation import MakeAnimation
from application.task.save_capsule_skin import SaveCapsuleSkin
from application.task.send_notification import SendNotification

queue_config = QueueConfig()
celery = Celery('application',
                broker=queue_config.get_queue_url(),
                include=['application.task'])

celery.conf.result_expires = 300
celery.conf.task_queues = (
    Queue('makeAnimation.queue'),
    Queue('saveCapsuleSkin.queue'),
    Queue('sendNotification.queue')
)

celery.register_task(MakeAnimation())
celery.register_task(SaveCapsuleSkin())
celery.register_task(SendNotification())
