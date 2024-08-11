package site.timecapsulearchive.notification.global.config.rabbitmq;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.timecapsulearchive.notification.global.error.InternalServerException;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitmqConfig {

    private final RabbitmqProperties rabbitmqProperties;

    @Bean
    public Queue capsuleSkinQueue() {
        return QueueBuilder.durable(
                RabbitmqComponentConstants.CAPSULE_SKIN_NOTIFICATION_QUEUE.getSuccessComponent())
            .withArgument("x-dead-letter-exchange",
                RabbitmqComponentConstants.CAPSULE_SKIN_NOTIFICATION_EXCHANGE.getFailComponent())
            .build();
    }

    @Bean
    public DirectExchange capsuleSkinExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.CAPSULE_SKIN_NOTIFICATION_EXCHANGE.getSuccessComponent());
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
        return QueueBuilder.durable(
                RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_QUEUE.getSuccessComponent())
            .withArgument("x-dead-letter-exchange",
                RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_EXCHANGE.getFailComponent())
            .build();
    }

    @Bean
    public DirectExchange groupInviteExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_EXCHANGE.getSuccessComponent());
    }

    @Bean
    public Binding groupInviteBinding() {
        return BindingBuilder
            .bind(groupInviteQueue())
            .to(groupInviteExchange())
            .withQueueName();
    }

    @Bean
    public Queue groupAcceptQueue() {
        return QueueBuilder.durable(
                RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_QUEUE.getSuccessComponent())
            .withArgument("x-dead-letter-exchange",
                RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_EXCHANGE.getFailComponent())
            .build();
    }

    @Bean
    public DirectExchange groupAcceptExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_EXCHANGE.getSuccessComponent());
    }

    @Bean
    public Binding groupAcceptBinding() {
        return BindingBuilder
            .bind(groupAcceptQueue())
            .to(groupAcceptExchange())
            .withQueueName();
    }

    @Bean
    public Queue friendRequestQueue() {
        return QueueBuilder.durable(
                RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getSuccessComponent())
            .withArgument("x-dead-letter-exchange",
                RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getFailComponent())
            .build();
    }

    @Bean
    public DirectExchange friendRequestExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getSuccessComponent());
    }

    @Bean
    public Binding friendRequestBinding() {
        return BindingBuilder
            .bind(friendRequestQueue())
            .to(friendRequestExchange())
            .withQueueName();
    }

    @Bean
    public Queue friendAcceptQueue() {
        return QueueBuilder.durable(
                RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_QUEUE.getSuccessComponent())
            .withArgument("x-dead-letter-exchange",
                RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_EXCHANGE.getFailComponent())
            .build();
    }

    @Bean
    public DirectExchange friendAcceptExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_EXCHANGE.getSuccessComponent());
    }

    @Bean
    public Binding friendAcceptBinding() {
        return BindingBuilder
            .bind(friendAcceptQueue())
            .to(friendAcceptExchange())
            .withQueueName();
    }

    @Bean
    public Queue batchFriendRequestsQueue() {
        return QueueBuilder.durable(
                RabbitmqComponentConstants.BATCH_FRIEND_REQUESTS_NOTIFICATION_QUEUE.getSuccessComponent())
            .withArgument("x-dead-letter-exchange",
                RabbitmqComponentConstants.BATCH_FRIEND_REQUESTS_NOTIFICATION_EXCHANGE.getFailComponent())
            .build();
    }

    @Bean
    public DirectExchange batchFriendRequestsExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.BATCH_FRIEND_REQUESTS_NOTIFICATION_EXCHANGE.getSuccessComponent());
    }

    @Bean
    public Binding batchFriendRequestsBinding() {
        return BindingBuilder
            .bind(batchFriendRequestsQueue())
            .to(batchFriendRequestsExchange())
            .withQueueName();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setHost(rabbitmqProperties.host());
        connectionFactory.setPort(rabbitmqProperties.port());
        connectionFactory.setUsername(rabbitmqProperties.userName());
        connectionFactory.setPassword(rabbitmqProperties.password());
        connectionFactory.setVirtualHost(rabbitmqProperties.virtualHost());

        if (rabbitmqProperties.isSslEnabled()) {
            try {
                connectionFactory.getRabbitConnectionFactory().useSslProtocol();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new InternalServerException(e);
            }
        }

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}