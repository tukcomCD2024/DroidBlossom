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
            value = @Queue(value = "capsuleSkin.queue", durable = "true"),
            exchange = @Exchange(value = "capsuleSkin.exchange"),
            key = "capsuleSkin.queue"
        ),
        returnExceptions = "false"
    )
    void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto);

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "friendRequest.queue", durable = "true"),
            exchange = @Exchange(value = "friendRequest.exchange"),
            key = "friendRequest.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendFriendRequestNotification(final FriendNotificationDto dto);


    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "friendRequests.queue", durable = "true"),
            exchange = @Exchange(value = "friendRequests.exchange"),
            key = "friendRequests.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    void sendFriendRequestNotifications(final FriendNotificationsDto dto);


    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "groupInvite.queue", durable = "true"),
            exchange = @Exchange(value = "groupInvite.exchange"),
            key = "groupInvite.queue"
        ),
        returnExceptions = "false"
    )
    void sendGroupAcceptNotification(final GroupInviteNotificationDto dto);

}
