package site.timecapsulearchive.notification.service.group;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;

public interface GroupAlarmListener {

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
