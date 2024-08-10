package site.timecapsulearchive.notification.global.error;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQErrorHandler implements RabbitListenerErrorHandler {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
        ListenerExecutionFailedException exception) {

        MessageProperties properties = amqpMessage.getMessageProperties();

        String routingKey = "fail." + properties.getReceivedRoutingKey();
        String exchange = "fail." + properties.getReceivedExchange();

        properties.setHeader("x-exception-message", exception.getCause().getMessage());
        properties.setHeader("x-original-exchange", properties.getReceivedExchange());
        properties.setHeader("x-original-routing-key", properties.getReceivedRoutingKey());

        rabbitTemplate.send(exchange, routingKey, amqpMessage);
        return null;
    }
}
