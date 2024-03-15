from urllib.parse import urlparse, quote_plus

from application.config.root_config import RootConfig


class DatabaseConfig(RootConfig):
    def __init__(self):
        self._db_username = self._config_file['spring']['datasource'][
            'username']
        self._db_password = self._config_file['spring']['datasource'][
            'password']
        self._db_url = urlparse(
            self._config_file['spring']['datasource']['url'])

    def get_database_url(self) -> str:
        url = self._db_url.path.split("//")[1]
        return 'mysql+pymysql://%s:%s@%s' % (
            self._db_username, quote_plus(self._db_password), url)
