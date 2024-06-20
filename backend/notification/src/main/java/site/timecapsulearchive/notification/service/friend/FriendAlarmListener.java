package site.timecapsulearchive.notification.service.friend;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;

public interface FriendAlarmListener {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "notification.friendRequest.queue", durable = "true"),
            exchange = @Exchange(value = "notification.friendRequest.exchange"),
            key = "notification.friendRequest.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendFriendRequestNotification(final FriendNotificationDto dto);

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "notification.friendAccept.queue", durable = "true"),
            exchange = @Exchange(value = "notification.friendAccept.exchange"),
            key = "notification.friendAccept.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendFriendAcceptNotification(final FriendNotificationDto dto);


    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "notification.friendRequests.queue", durable = "true"),
            exchange = @Exchange(value = "notification.friendRequests.exchange"),
            key = "notification.friendRequests.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendFriendRequestNotifications(final FriendNotificationsDto dto);

}
