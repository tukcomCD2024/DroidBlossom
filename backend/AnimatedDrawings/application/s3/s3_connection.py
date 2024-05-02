import boto3
from botocore.config import Config

from application.config.s3_config import S3Config
from application.s3.object_wrapper import ObjectWrapper


def s3_resource():
    return boto3.resource('s3', aws_access_key_id=S3Config.ACCESS_KEY_ID,
                          aws_secret_access_key=S3Config.SECRET_ACCESS_KEY,
                          config=Config(signature_version='s3v4'))


def get_object_wrapper(bucket: str, key: str) -> ObjectWrapper:
    return ObjectWrapper(s3_resource().Object(bucket, key))
