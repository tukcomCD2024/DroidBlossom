import os
import shutil
from pathlib import Path

import celery.signals
import requests
from celery.utils.log import get_task_logger

from application.config.logger_config import LoggerConfig
from application.config.s3_config import S3Config
from application.logging.logger_factory import LoggerFactory
from application.s3.s3_connection import get_object_wrapper
from application.task.base_task import LogErrorsTask
from examples.annotations_to_animation import annotations_to_animation
from examples.image_to_annotations import image_to_annotations


class MakeAnimation(LogErrorsTask):
    name = 'make_animation'

    def __init__(self):
        self.s3_bucket_name = S3Config.S3_BUCKET_NAME
        self.task_logger = get_task_logger(__name__)

    @celery.signals.after_setup_task_logger.connect
    def on_after_setup_logger(logger, **kwargs):
        LoggerFactory.setup_logger(logger, LoggerConfig.CELERY_OUTPUT_FILE_PATH)

    def run(self, *args, **kwargs):
        self.task_logger.debug('애니메이션 생성 시작')
        img_bytes = requests.get(kwargs['input_data']['imageUrl']).content

        output_directory = 'capsuleSkin/' + kwargs['input_data']['memberId']
        result = Path(output_directory)
        result.mkdir(exist_ok=True)

        image_to_annotations(img_bytes, result)
        annotations_to_animation(output_directory,
                                 kwargs['input_data']['motionName'],
                                 kwargs['input_data']['retarget'])

        with open(output_directory + '/video.gif', 'rb') as image:
            gif_bytes = bytearray(image.read())

        output_wrapper = get_object_wrapper(self.s3_bucket_name,
                                            kwargs['filename'])

        output_wrapper.put(gif_bytes)

        if os.path.exists(output_directory):
            shutil.rmtree(output_directory)
        self.task_logger.debug('애니메이션 생성 완료')
