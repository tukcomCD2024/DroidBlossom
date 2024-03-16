import logging

import requests

from application.model.notification_status import \
    NotificationStatus
from application.task.base_task import LogErrorsTask

logger = logging.getLogger('send_notification')


class SendNotification(LogErrorsTask):
    name = 'send_notification'

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
            logger.error('알림 서버 동작 오류 request: %s, response: %s', ex.request, ex.response)
