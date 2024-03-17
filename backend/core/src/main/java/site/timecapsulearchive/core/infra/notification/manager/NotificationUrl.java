package site.timecapsulearchive.core.infra.notification.manager;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification-url")
public record NotificationUrl(
    String capsuleSkinAlarmUrl,
    String friendReqAlarmUrl
) {

}
