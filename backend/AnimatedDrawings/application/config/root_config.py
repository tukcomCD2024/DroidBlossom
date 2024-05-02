import os

import yaml

environment = os.environ.get('ENVIRONMENT', 'local')
APPLICATION_YAML_PATH = f'config/yml/config-{environment}.yml'


class RootConfig:
    CONFIG_FILE = None

    if CONFIG_FILE is None:
        with open(APPLICATION_YAML_PATH) as file:
            CONFIG_FILE = yaml.safe_load(file)
