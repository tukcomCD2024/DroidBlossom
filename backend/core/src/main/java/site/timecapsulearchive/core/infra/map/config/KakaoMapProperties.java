package site.timecapsulearchive.core.infra.map.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoMapProperties(
    String apiKey
) {

}
