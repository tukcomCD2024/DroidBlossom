package site.timecapsulearchive.core.global.config.rabbitmq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitmqProperties(
    String host,
    int port,
    String userName,
    String password,
    String virtualHost,
    ConfirmType publisherConfirmType,
    boolean publisherReturns,
    @NestedConfigurationProperty
    SSL ssl
) {
    protected record SSL(
        boolean enabled
    ) {

    }

    public boolean isSslEnabled() {
        return ssl != null && ssl.enabled;
    }
}