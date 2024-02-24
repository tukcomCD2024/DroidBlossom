package site.timecapsulearchive.notification.infra.fcm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fcm")
public record FCMProperties(String secretKeyPath, String projectId) {

}