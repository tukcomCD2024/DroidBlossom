from application.config.root_config import RootConfig


class QueueConfig:
    USERNAME = RootConfig.CONFIG_FILE['rabbitmq']['username']
    BROKER_HOST = RootConfig.CONFIG_FILE['rabbitmq']['host']
    PASSWORD = RootConfig.CONFIG_FILE['rabbitmq']['password']
    VIRTUAL_HOST = RootConfig.CONFIG_FILE['rabbitmq']['virtual-host']
    CAPSULE_SKIN_REQUEST_QUEUE_NAME = RootConfig.CONFIG_FILE['rabbitmq'][
        'queue_name']
    CAPSULE_SKIN_REQUEST_EXCHANGE_NAME = RootConfig.CONFIG_FILE['rabbitmq'][
        'exchange_name']
    NOTIFICATION_EXCHANGE_NAME = RootConfig.CONFIG_FILE['rabbitmq'][
        'notification_exchange_name']
    NOTIFICATION_QUEUE_NAME = RootConfig.CONFIG_FILE['rabbitmq'][
        'notification_queue_name']

    @staticmethod
    def get_celery_broker_url() -> str:
        return 'pyamqp://%s:%s@%s:5672%s' % (QueueConfig.USERNAME,
                                             QueueConfig.PASSWORD,
                                             QueueConfig.BROKER_HOST,
                                             QueueConfig.VIRTUAL_HOST)

    @staticmethod
    def get_kombu_broker_url() -> str:
        return f'amqp://{QueueConfig.USERNAME}:{QueueConfig.PASSWORD}@{QueueConfig.BROKER_HOST}/{QueueConfig.VIRTUAL_HOST}'
