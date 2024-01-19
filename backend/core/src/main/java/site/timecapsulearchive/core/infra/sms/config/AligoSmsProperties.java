package site.timecapsulearchive.core.infra.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aligo")
public record AligoSmsProperties(
    String userId,
    String apiKey,
    String sender,
    String testmodeYn
) {

}
