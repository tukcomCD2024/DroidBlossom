package site.timecapsulearchive.notification.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitmqProperties(
    String host,
    int port,
    String userName,
    String password,
    String virtualHost
) {

}
