package site.timecapsulearchive.notification.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitmqProperties(
    String host,
    int port,
    String userName,
    String password,
    String virtualHost,
    @NestedConfigurationProperty
    SSL ssl
) {
    protected record SSL(
        boolean enabled
    ) {

    }

    public Boolean isSslEnabled() {
        return ssl != null && ssl.enabled;
    }
}