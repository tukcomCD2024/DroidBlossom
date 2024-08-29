package site.timecapsulearchive.core.global.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record DefaultKeyProperties(
    String defaultKey
) {

}
