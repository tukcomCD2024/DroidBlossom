package site.timecapsulearchive.core.infra.kakao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoMapProperties(
    String apiKey
) {

}
