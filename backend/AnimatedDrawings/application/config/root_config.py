import os

import yaml

environment = os.environ.get('ENVIRONMENT', 'local')
APPLICATION_YAML_PATH = f'config/yaml/application-{environment}.yml'


class RootConfig:
    with open(APPLICATION_YAML_PATH) as file:
        _config_file = yaml.safe_load(file)
