package site.timecapsulearchive.core.global.api.limit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
public record ApiLimitProperties(
    int smsLimit
) {

}
