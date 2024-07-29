from application.config.root_config import RootConfig


class QueueConfig:
    PROTOCOL = RootConfig.CONFIG_FILE['rabbitmq']['protocol']
    USERNAME = RootConfig.CONFIG_FILE['rabbitmq']['username']
    BROKER_HOST = RootConfig.CONFIG_FILE['rabbitmq']['host']
    PASSWORD = RootConfig.CONFIG_FILE['rabbitmq']['password']
    PORT = RootConfig.CONFIG_FILE['rabbitmq']['port']
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
    def get_broker_url() -> str:
        return (
            f'{QueueConfig.PROTOCOL}://'
            f'{QueueConfig.USERNAME}:'
            f'{QueueConfig.PASSWORD}@'
            f'{QueueConfig.BROKER_HOST}:'
            f'{QueueConfig.PORT}'
            f'{QueueConfig.VIRTUAL_HOST}'
        )
