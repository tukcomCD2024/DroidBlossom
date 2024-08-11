package site.timecapsulearchive.notification.service.friend;

import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;

public interface FriendAlarmListener {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(
                value = "notification.friendRequest.queue",
                durable = "true",
                arguments = @Argument(name = "x-dead-letter-exchange", value = "fail.notification.friendRequest.exchange")
            ),
            exchange = @Exchange(value = "notification.friendRequest.exchange"),
            key = "notification.friendRequest.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter",
        errorHandler = "rabbitMQErrorHandler"
    )
    void sendFriendRequestNotification(final FriendNotificationDto dto);

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(
                value = "notification.friendAccept.queue",
                durable = "true",
                arguments = @Argument(name = "x-dead-letter-exchange", value = "fail.notification.friendAccept.exchange")
            ),
            exchange = @Exchange(value = "notification.friendAccept.exchange"),
            key = "notification.friendAccept.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter",
        errorHandler = "rabbitMQErrorHandler"
    )
    void sendFriendAcceptNotification(final FriendNotificationDto dto);


    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(
                value = "batch.notification.friendRequests.queue",
                durable = "true",
                arguments = @Argument(name = "x-dead-letter-exchange", value = "fail.batch.notification.friendRequests.exchange")
            ),
            exchange = @Exchange(value = "batch.notification.friendRequests.exchange"),
            key = "batch.notification.friendRequests.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter",
        errorHandler = "rabbitMQErrorHandler"
    )
    void sendFriendRequestNotifications(final FriendNotificationsDto dto);

}
