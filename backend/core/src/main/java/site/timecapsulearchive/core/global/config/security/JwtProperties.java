package site.timecapsulearchive.core.global.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String secretKey,
    long accessTokenValidityMs,
    long refreshTokenValidityMs,
    long temporaryTokenValidityMs
) {

}