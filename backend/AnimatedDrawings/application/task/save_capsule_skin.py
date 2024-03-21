from sqlalchemy import create_engine
from sqlalchemy.orm import Session

from application.config.database_config import DatabaseConfig
from application.task.base_task import LogErrorsTask
from application.model.capsule_skin import CapsuleSkin
from application.model.motion import Motion
from application.model.retarget import Retarget


class SaveCapsuleSkin(LogErrorsTask):
    name = 'save_capsule_skin'
    database_config = DatabaseConfig()
    engine = create_engine(database_config.get_database_url())

    def run(self, *args, **kwargs):
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
