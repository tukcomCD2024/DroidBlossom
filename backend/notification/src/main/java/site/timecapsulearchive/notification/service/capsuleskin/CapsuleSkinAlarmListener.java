package site.timecapsulearchive.notification.service.capsuleskin;

import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;

public interface CapsuleSkinAlarmListener {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(
                value = "notification.createCapsuleSkin.queue",
                durable = "true",
                arguments = @Argument(name = "x-dead-letter-exchange", value = "fail.notification.createCapsuleSkin.exchange")
            ),
            exchange = @Exchange(value = "notification.createCapsuleSkin.exchange"),
            key = "notification.createCapsuleSkin.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter",
        errorHandler = "rabbitMQErrorHandler"
    )
    void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto);

}
