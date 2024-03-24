import requests
import celery.signals
from celery.utils.log import get_task_logger

from application.logging.logger_factory import LoggerFactory
from application.model.notification_status import NotificationStatus
from application.task.base_task import LogErrorsTask


class SendNotification(LogErrorsTask):
    name = 'send_notification'

    def __init__(self):
        super().__init__()
        self.task_logger = get_task_logger(__name__)

    @celery.signals.after_setup_task_logger.connect
    def on_after_setup_logger(logger, **kwargs):
        LoggerFactory.setup_logger(logger)

    def run(self, *args, **kwargs):
        request_data = {
            'memberId': kwargs['input_data']['memberId'],
            'skinName': kwargs['input_data']['skinName'],
            'title': '캡슐 스킨 생성이 완료되었습니다',
            'text': f"{kwargs['input_data']['skinName']}이 생성되었습니다. ARchive에서 확인해보세요!",
            'skinUrl': kwargs['filename'],
            'status': NotificationStatus.SUCCESS.value
        }
        try:
            r = requests.post(self.notification_server_url,
                              json=request_data,
                              verify=False,
                              timeout=5)
            r.raise_for_status()
        except requests.exceptions.HTTPError as ex:
            self.task_logger.exception('알림 서버 동작 오류', exc_info=ex)
