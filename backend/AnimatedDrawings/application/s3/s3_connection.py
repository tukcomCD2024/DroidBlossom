import boto3
from botocore.config import Config

from application.config.s3_config import S3Config
from application.s3.object_wrapper import ObjectWrapper

s3_config = S3Config()


def s3_resource():
    return boto3.resource('s3', aws_access_key_id=s3_config.access_key,
                          aws_secret_access_key=s3_config.secret_key,
                          config=Config(signature_version='s3v4'))


def get_object_wrapper(bucket: str, key: str) -> ObjectWrapper:
    return ObjectWrapper(s3_resource().Object(bucket, key))
