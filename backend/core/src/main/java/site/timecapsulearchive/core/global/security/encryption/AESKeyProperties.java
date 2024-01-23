package site.timecapsulearchive.core.global.security.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aes")
public record AESKeyProperties(String secretKey) {

}

