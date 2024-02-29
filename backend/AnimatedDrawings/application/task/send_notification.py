import requests

from application.task.base_task import LogErrorsTask


class SendNotification(LogErrorsTask):
    name = 'send_notification'

    def __init__(self):
        self.title = '캡슐 스킨 생성이 완료되었습니다!'
        self.notification_server_url = 'https://notification.archive-timecapsule.kro.kr/api/notification/capsule_skin/send'

    def run(self, *args, **kwargs):
        request_data = {
            'memberId': kwargs['input_data']['memberId'],
            'skinName': kwargs['input_data']['skinName'],
            'title': self.title,
            'text': f"{kwargs['input_data']['skinName']}이 생성되었습니다. ARchive에서 확인해보세요!",
            'skinUrl': kwargs['filename']
        }

        requests.post(self.notification_server_url,
                      json=request_data,
                      verify=False,
                      timeout=5)
