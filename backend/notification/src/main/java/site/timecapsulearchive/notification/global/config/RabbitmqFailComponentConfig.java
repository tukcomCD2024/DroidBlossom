package site.timecapsulearchive.notification.global.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqFailComponentConfig {

    @Bean
    public Queue capsuleSkinFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.CAPSULE_SKIN_NOTIFICATION_QUEUE.getFailComponent(), true);
    }

    @Bean
    public FanoutExchange capsuleSkinFailExchange() {
        return new FanoutExchange(
            RabbitmqComponentConstants.CAPSULE_SKIN_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding capsuleSkinFailBinding() {
        return BindingBuilder
            .bind(capsuleSkinFailQueue())
            .to(capsuleSkinFailExchange());
    }

    @Bean
    public Queue groupInviteFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_QUEUE.getFailComponent(), true);
    }

    @Bean
    public FanoutExchange groupInviteFailExchange() {
        return new FanoutExchange(
            RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding groupInviteFailBinding() {
        return BindingBuilder
            .bind(groupInviteFailQueue())
            .to(groupInviteFailExchange());
    }


    @Bean
    public Queue groupAcceptFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_QUEUE.getFailComponent(), true);
    }

    @Bean
    public FanoutExchange groupAcceptFailExchange() {
        return new FanoutExchange(
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding groupAcceptFailBinding() {
        return BindingBuilder
            .bind(groupAcceptFailQueue())
            .to(groupAcceptFailExchange());
    }


    @Bean
    public Queue friendRequestFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getFailComponent(),
            true);
    }

    @Bean
    public FanoutExchange friendRequestFailExchange() {
        return new FanoutExchange(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding friendRequestFailBinding() {
        return BindingBuilder
            .bind(friendRequestFailQueue())
            .to(friendRequestFailExchange());
    }

    @Bean
    public Queue friendAcceptFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_QUEUE.getFailComponent(),
            true);
    }

    @Bean
    public FanoutExchange friendAcceptFailExchange() {
        return new FanoutExchange(
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding friendAcceptFailBinding() {
        return BindingBuilder
            .bind(friendAcceptFailQueue())
            .to(friendAcceptFailExchange());
    }

    @Bean
    public Queue batchFriendRequestsFailQueue() {
        return new Queue(
            RabbitmqComponentConstants.BATCH_FRIEND_REQUESTS_NOTIFICATION_QUEUE.getFailComponent(),
            true);
    }

    @Bean
    public FanoutExchange batchFriendRequestsFailExchange() {
        return new FanoutExchange(
            RabbitmqComponentConstants.BATCH_FRIEND_REQUESTS_NOTIFICATION_EXCHANGE.getFailComponent());
    }

    @Bean
    public Binding batchFriendRequestsFailBinding() {
        return BindingBuilder
            .bind(batchFriendRequestsFailQueue())
            .to(batchFriendRequestsFailExchange());
    }
}