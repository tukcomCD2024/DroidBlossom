package site.timecapsulearchive.core.global.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq")
public record RabbitmqProperties(
    String host,
    int port,
    String userName,
    String password,
    String virtualHost
) {

}
