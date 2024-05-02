from sqlalchemy import URL

from application.config.root_config import RootConfig


class DatabaseConfig:
    USER_NAME = RootConfig.CONFIG_FILE['database']['username']
    PASSWORD = RootConfig.CONFIG_FILE['database']['password']
    DB_URL = RootConfig.CONFIG_FILE['database']['url']
    DB_NAME = RootConfig.CONFIG_FILE['database']['database_name']

    @staticmethod
    def get_database_url() -> URL:
        url_object = URL.create(
            "mysql+pymysql",
            username=DatabaseConfig.USER_NAME,
            password=DatabaseConfig.PASSWORD,
            host=DatabaseConfig.DB_URL,
            database=DatabaseConfig.DB_NAME
        )
        return url_object
