package site.timecapsulearchive.core.global.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConfig {

    private static final int MAX_RETRY_COUNT = 3;
    private static final String RETRY_HEADER = "x-retry-count";

    private final RabbitmqProperties rabbitmqProperties;

    @Bean
    public Queue capsuleSkinQueue() {
        return QueueBuilder.durable(RabbitmqComponentConstants.CAPSULE_SKIN_QUEUE.getValue())
            .deadLetterExchange(RabbitmqComponentConstants.CAPSULE_SKIN_DELAY_EXCHANGE.getValue())
            .ttl(5000)
            .build();
    }

    @Bean
    public DirectExchange capsuleSkinExchange() {
        return new DirectExchange(RabbitmqComponentConstants.CAPSULE_SKIN_EXCHANGE.getValue());
    }

    @Bean
    public Binding capsuleSkinBinding() {
        return BindingBuilder
            .bind(capsuleSkinQueue())
            .to(capsuleSkinExchange())
            .withQueueName();
    }

    @Bean
    public Queue groupInviteQueue() {
        return new Queue(RabbitmqComponentConstants.GROUP_INVITE_QUEUE_NAME.getValue(), true);
    }

    @Bean
    public DirectExchange groupInviteExchange() {
        return new DirectExchange(RabbitmqComponentConstants.GROUP_INVITE_EXCHANGE_NAME.getValue());
    }

    @Bean
    public Binding groupInviteBinding() {
        return BindingBuilder
            .bind(groupInviteQueue())
            .to(groupInviteExchange())
            .withQueueName();
    }

    @Bean
    public Queue friendRequestQueue() {
        return new Queue(RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getValue(),
            true);
    }

    @Bean
    public DirectExchange friendRequestExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getValue());
    }

    @Bean
    public Binding friendRequestQueueAndExchangeBinding() {
        return BindingBuilder
            .bind(friendRequestQueue())
            .to(friendRequestExchange())
            .withQueueName();
    }

    @Bean
    public RabbitTemplate publisherConfirmsRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(publisherConfirmsConnectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReturnsCallback(returned -> {
            Message message = returned.getMessage();

            Integer retryCount = message.getMessageProperties()
                .getHeader(RETRY_HEADER);

            if (retryCount == null) {
                retryCount = 0;
            } else if (retryCount > MAX_RETRY_COUNT) {
                final String routingKey = returned.getRoutingKey();

                final String failRoutingKey = RabbitmqComponentConstants.getFailComponent(routingKey);
                final String failExchange = RabbitmqComponentConstants.getFailComponent(routingKey);

                rabbitTemplate.send(failExchange, failRoutingKey, message);
                return;
            }

            message.getMessageProperties().setHeader(RETRY_HEADER, retryCount + 1);
            rabbitTemplate.send(message);
        });
        return rabbitTemplate;
    }

    @Bean
    public CachingConnectionFactory publisherConfirmsConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setHost(rabbitmqProperties.host());
        connectionFactory.setPort(rabbitmqProperties.port());
        connectionFactory.setUsername(rabbitmqProperties.userName());
        connectionFactory.setPassword(rabbitmqProperties.password());
        connectionFactory.setVirtualHost(rabbitmqProperties.virtualHost());
        connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate basicRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(publisherConfirmsConnectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
