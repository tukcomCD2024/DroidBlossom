from application.config.root_config import RootConfig


class TorchserveConfig(RootConfig):
    def __init__(self):
        self._torchserve_host = self._config_file['docker_torchserve']['host']

    @property
    def torchserve_host(self) -> str:
        return self._torchserve_host
