from application.config.root_config import RootConfig


class S3Config(RootConfig):
    ACCESS_KEY_ID = RootConfig.CONFIG_FILE['s3']['accessKey']
    SECRET_ACCESS_KEY = RootConfig.CONFIG_FILE['s3']['secretKey']
    S3_BUCKET_NAME = RootConfig.CONFIG_FILE['s3']['bucket']
