from application.config.root_config import RootConfig


class FlowerConfig(RootConfig):
    def __init__(self):
        self._username = self._config_file['flower']['username']
        self._password = self._config_file['flower']['password']

    @property
    def username(self):
        return self._username

    @property
    def password(self):
        return self._password


config = FlowerConfig()
basic_auth = "%s:%s" % (config.username, config.password)
