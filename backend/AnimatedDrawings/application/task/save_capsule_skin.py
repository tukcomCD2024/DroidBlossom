import celery.signals
from celery.utils.log import get_task_logger
from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from application.config.database_config import DatabaseConfig
from application.config.logger_config import LoggerConfig
from application.logging.logger_factory import LoggerFactory
from application.model.capsule_skin import CapsuleSkin
from application.model.motion import Motion
from application.model.retarget import Retarget
from application.task.base_task import LogErrorsTask


class SaveCapsuleSkin(LogErrorsTask):
    name = 'save_capsule_skin'
    database_config = DatabaseConfig()
    engine = create_engine(DatabaseConfig.get_database_url())

    def __init__(self):
        self.task_logger = get_task_logger(__name__)

    @celery.signals.after_setup_task_logger.connect
    def on_after_setup_logger(logger, **kwargs):
        LoggerFactory.setup_logger(logger, LoggerConfig.CELERY_OUTPUT_FILE_PATH)

    def run(self, *args, **kwargs):
        self.task_logger.debug('캡슐 스킨 DB 저장 시작')
        capsule_skin = CapsuleSkin(skin_name=kwargs['input_data']['skinName'],
                                   image_url=kwargs['filename'],
                                   motion_name=Motion(
                                       kwargs['input_data']['motionName']).name,
                                   retarget=Retarget(
                                       kwargs['input_data']['retarget']).name,
                                   member_id=kwargs['input_data']['memberId'])

        with Session(self.engine) as session:
            session.add(capsule_skin)
            session.commit()
        self.task_logger.debug('캡슐 스킨 DB 저장 완료')
