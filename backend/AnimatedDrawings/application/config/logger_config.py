from application.config.root_config import RootConfig


class LoggerConfig:
    LOGGING_LEVEL = RootConfig.CONFIG_FILE['logging']['level']
    CELERY_OUTPUT_FILE_PATH = RootConfig.CONFIG_FILE['logging'][
        'celery_output_file_path']
    APPLICATION_OUTPUT_FILE_PATH = RootConfig.CONFIG_FILE['logging'][
        'application_output_file_path']
    FORMAT_STRING = RootConfig.CONFIG_FILE['logging']['format']
