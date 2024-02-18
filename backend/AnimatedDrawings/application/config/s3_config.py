from application.config.root_config import RootConfig


class S3Config(RootConfig):
    def __init__(self):
        self._access_key_id = self._config_file['s3']['accessKey']
        self._secret_access_key = self._config_file['s3']['secretKey']
        self._s3_bucket_name = self._config_file['s3']['bucket']

    @property
    def access_key(self):
        return self._access_key_id

    @property
    def secret_key(self):
        return self._secret_access_key

    @property
    def s3_bucket_name(self):
        return self._s3_bucket_name
