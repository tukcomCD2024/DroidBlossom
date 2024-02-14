import boto3

from object_wrapper import ObjectWrapper


def s3_connection():
    return boto3.resource('s3')


def get_object_wrapper(bucket: str, key: str) -> ObjectWrapper:
    return ObjectWrapper(s3_connection().Object(bucket, key))
