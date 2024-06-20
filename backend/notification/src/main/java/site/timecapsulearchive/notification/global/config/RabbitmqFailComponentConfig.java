package site.timecapsulearchive.notification.global.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqFailComponentConfig {

    @Bean
    public Queue capsuleSkinFailQueue() {
        return new Queue(RabbitmqComponentConstants.CAPSULE_SKIN_QUEUE.getFailComponent(), true);
    }

    @Bean
    public DirectExchange capsuleSkinFailExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.CAPSULE_SKIN_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding capsuleSkinFailBinding() {
        return BindingBuilder
            .bind(capsuleSkinFailQueue())
            .to(capsuleSkinFailExchange())
            .withQueueName();
    }

    @Bean
    public Queue groupInviteFailQueue() {
        return new Queue(RabbitmqComponentConstants.GROUP_INVITE_QUEUE.getFailComponent(), true);
    }

    @Bean
    public DirectExchange groupInviteFailExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.GROUP_INVITE_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding groupInviteFailBinding() {
        return BindingBuilder
            .bind(groupInviteFailQueue())
            .to(groupInviteFailExchange())
            .withQueueName();
    }


    @Bean
    public Queue groupAcceptFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_QUEUE.getFailComponent(), true);
    }

    @Bean
    public DirectExchange groupAcceptFailExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding groupAcceptFailBinding() {
        return BindingBuilder
            .bind(groupAcceptFailQueue())
            .to(groupAcceptFailExchange())
            .withQueueName();
    }


    @Bean
    public Queue friendRequestFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getFailComponent(),
            true);
    }

    @Bean
    public DirectExchange friendRequestFailExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding friendRequestFailBinding() {
        return BindingBuilder
            .bind(friendRequestFailQueue())
            .to(friendRequestFailExchange())
            .withQueueName();
    }

    @Bean
    public Queue friendAcceptFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_QUEUE.getFailComponent(),
            true);
    }

    @Bean
    public DirectExchange friendAcceptFailExchange() {
        return new DirectExchange(
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding friendAcceptFailBinding() {
        return BindingBuilder
            .bind(friendAcceptFailQueue())
            .to(friendAcceptFailExchange())
            .withQueueName();
    }
}