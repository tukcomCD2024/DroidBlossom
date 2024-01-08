from celery import Celery
from kombu import Queue
from examples.image_to_annotations import image_to_annotations
from examples.annotations_to_animation import annotations_to_animation
from celery.utils.log import get_task_logger

logger = get_task_logger(__name__)

celery = Celery('tasks',
                broker='pyamqp://guest:guest@rabbit:5672/',
                backend='redis://ai_redis:6000/0',
                include=["tasks"])

celery.conf.result_expires = 300
celery.conf.task_queues = (
  Queue('animation_tasks', routing_key='animation.#'),
  Queue("success_tasks", routing_key='success.#'),
)


@celery.task
def make_animation(
    file: bytes,
    char_anno_dir: str,
    motion_cfg_fn: str,
    retarget_cfg_fn: str) -> str:
  # create the annotations
  image_to_annotations(file, char_anno_dir)

  logger.info("change")

  # create the animation
  annotations_to_animation(char_anno_dir, motion_cfg_fn, retarget_cfg_fn)

  logger.info("finish")
  return "success"


@celery.task
def success(msg):
  logger.info("start success")
  logger.info(str(msg))

  return "suces"
