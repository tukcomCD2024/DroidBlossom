from application.config.root_config import RootConfig


class DatabaseConfig(RootConfig):
    def __init__(self):
        self._db_username = self._config_file['spring']['datasource']['username']
        self._db_password = self._config_file['spring']['datasource']['password']
        self._db_url = self._config_file['spring']['datasource']['url'].split('//')[1]

    def get_database_url(self) -> str:
        return "mysql+pymysql://%s:%s@%s" % (
            self._db_username, self._db_password, self._db_url)
