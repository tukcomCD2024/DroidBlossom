package site.timecapsulearchive.notification.service;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;

public interface NotificationServiceListener {


    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "notification.createCapsuleSkin.queue", durable = "true"),
            exchange = @Exchange(value = "notification.createCapsuleSkin.exchange"),
            key = "notification.createCapsuleSkin.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto);

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


    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "notification.groupInvite.queue", durable = "true"),
            exchange = @Exchange(value = "groupInvite.exchange"),
            key = "notification.groupInvite.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendGroupInviteNotification(final GroupInviteNotificationDto dto);

}
