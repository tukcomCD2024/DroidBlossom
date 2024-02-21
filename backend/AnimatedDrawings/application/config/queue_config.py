from application.config.root_config import RootConfig


class QueueConfig(RootConfig):
    def __init__(self):
        self._queue_username = self._config_file['spring']['rabbitmq'][
            'username']
        self._queue_host = self._config_file['spring']['rabbitmq']['host']
        self._queue_password = self._config_file['spring']['rabbitmq'][
            'password']
        self._queue_virtual_host = self._config_file['spring']['rabbitmq'][
            'virtual-host']
        self._queue_name = 'capsuleSkin.queue'

    def get_queue_url(self) -> str:
        return 'pyamqp://%s:%s@%s:5672%s' % (self._queue_username,
                                             self._queue_password,
                                             self._queue_host,
                                             self._queue_virtual_host)

    @property
    def queue_name(self) -> str:
        return self._queue_name

    @property
    def queue_host(self) -> str:
        return self._queue_host
