import logging

from botocore.exceptions import ClientError


class ObjectWrapper:
    def __init__(self, s3_object):
        self.object = s3_object
        self.key = self.object.key

    def put(self, data: bytes):
        put_data = data
        if isinstance(data, str):
            try:
                put_data = open(data, "rb")
            except IOError:
                logging.exception("데이터가 올바르지 않습니다. '%s'.", data)
                raise

        try:
            self.object.put(Body=put_data)
            self.object.wait_until_exists()
            logging.debug(
                "버킷 '%s'에 '%s'를 저장합니다.",
                self.object.bucket_name,
                self.object.key,
            )
        except ClientError:
            logging.exception(
                "버킷 '%s'에 '%s'를 저장할 수 없습니다.",
                self.object.bucket_name,
                self.object.key,
            )
            raise
        finally:
            if getattr(put_data, "close", None):
                put_data.close()
