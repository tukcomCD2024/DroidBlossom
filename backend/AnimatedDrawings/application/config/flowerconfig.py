from application.config.root_config import RootConfig


class FlowerConfig:
    USERNAME = RootConfig.CONFIG_FILE['flower']['username']
    PASSWORD = RootConfig.CONFIG_FILE['flower']['password']


basic_auth = f'{FlowerConfig.USERNAME}:{FlowerConfig.PASSWORD}'
