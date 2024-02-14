package site.timecapsulearchive.core.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
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
public class RabbitmqConfig {


    private static final String CAPSULE_SKIN_QUEUE_NAME = "capsuleSkin.queue";
    public static final String CAPSULE_SKIN_EXCHANGE_NAME = "capsuleSkin.exchange";
    private static final String ROUTING_KEY = "capsuleSkin.#";

    @Bean
    public Queue queue() {
        return new Queue(CAPSULE_SKIN_QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(CAPSULE_SKIN_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
            .bind(queue())
            .to(exchange())
            .with(ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReplyErrorHandler(errorHandler());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5637);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        return factory;
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

    public String getCapsuleSkinExchangeName() {
        return CAPSULE_SKIN_EXCHANGE_NAME;
    }

    public String getRoutingKey() {
        return ROUTING_KEY;
    }
}
