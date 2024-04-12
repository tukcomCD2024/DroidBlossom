package site.timecapsulearchive.core.global.config.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RabbitmqConfig {

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
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReplyErrorHandler(errorHandler());
        return rabbitTemplate;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setHost(rabbitmqProperties.host());
        connectionFactory.setPort(rabbitmqProperties.port());
        connectionFactory.setUsername(rabbitmqProperties.userName());
        connectionFactory.setPassword(rabbitmqProperties.password());
        connectionFactory.setVirtualHost(rabbitmqProperties.virtualHost());

        return connectionFactory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
    }

    public static class MyFatalExceptionStrategy extends DefaultExceptionStrategy {

        @Override
        public boolean isFatal(Throwable throwable) {
            if (throwable instanceof ListenerExecutionFailedException exception) {
                logger.error("Failed to process inbound message from queue "
                    + exception.getFailedMessage().getMessageProperties().getConsumerQueue()
                    + "; failed message: " + exception.getFailedMessage(), throwable);
            }
            return super.isFatal(throwable);
        }

    }
}
