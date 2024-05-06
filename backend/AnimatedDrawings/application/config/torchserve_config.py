from application.config.root_config import RootConfig


class TorchserveConfig:
    TORCHSERVE_HOST = RootConfig.CONFIG_FILE['docker_torchserve']['host']
