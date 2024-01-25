package site.timecapsulearchive.core.global.security.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hash")
public record HashProperties(String salt) {

}
