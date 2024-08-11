from kombu import Connection, Queue, Exchange, pools

from application.config.queue_config import QueueConfig
from application.logging.logger_factory import LoggerFactory

logger = LoggerFactory.get_logger(__name__)

"""
kombu 커넥션 풀 설정 및 큐, 익스체인지 선언
"""


def errback(exc, interval):
    logger.error('레빗 엠큐 커넥션 풀 연결 에러 발생 : %r', exc)
    logger.info('레빗 엠큐 커넥션 풀 연결 재시도: %s초 후', interval)


connections = pools.Connections(limit=2)
producers = pools.Producers(limit=2)

if QueueConfig.PROTOCOL == 'amqps':
    import ssl

    ssl_option = {
        'cert_reqs': ssl.CERT_REQUIRED
    }
else:
    ssl_option = None
connection = Connection(hostname=QueueConfig.get_broker_url(),
                        connect_timeout=7,
                        ssl=ssl_option,
                        errback=errback)

logger.info('레빗 엠큐 커넥션 풀 연결 설정 완료')

with connections[connection].acquire(block=True) as conn:
    with conn.channel() as channel:
        capsule_skin_exchange = Exchange(
            name=QueueConfig.CAPSULE_SKIN_REQUEST_EXCHANGE_NAME,
            type='direct',
            durable=True)
        capsule_skin_queue = Queue(
            name=QueueConfig.CAPSULE_SKIN_REQUEST_QUEUE_NAME,
            exchange=capsule_skin_exchange,
            routing_key=QueueConfig.CAPSULE_SKIN_REQUEST_QUEUE_NAME)
        capsule_skin_queue.declare(channel=channel)

        notification_dlx_name = f'fail.{QueueConfig.NOTIFICATION_EXCHANGE_NAME}'
        notification_dlq_name = f'fail.{QueueConfig.NOTIFICATION_QUEUE_NAME}'
        notification_dlx = Exchange(
            name=notification_dlx_name,
            type='fanout',
            durable=True)
        notification_dlq = Queue(
            name=notification_dlq_name,
            exchange=notification_dlx,
            routing_key=notification_dlx_name)
        notification_dlq.declare(channel=channel)

        notification_exchange = Exchange(
            name=QueueConfig.NOTIFICATION_EXCHANGE_NAME,
            type='direct',
            durable=True)

        notification_queue = Queue(name=QueueConfig.NOTIFICATION_QUEUE_NAME,
                                   exchange=notification_exchange,
                                   routing_key=QueueConfig.NOTIFICATION_QUEUE_NAME,
                                   queue_arguments={
                                       'x-dead-letter-exchange': notification_dlx_name,
                                   })
        notification_queue.declare(channel=channel)
logger.info('레빗 엠큐 큐, 익스체인지 설정 완료')
