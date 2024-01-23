package site.timecapsulearchive.core.global.security.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "phone")
public record PhoneNumberKeyProperties(String key) {

}
