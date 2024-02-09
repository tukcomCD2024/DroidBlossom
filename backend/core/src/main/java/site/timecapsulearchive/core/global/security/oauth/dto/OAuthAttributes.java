package site.timecapsulearchive.core.global.security.oauth.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.oauth.dto.google.GoogleOAuth2UserInfo;
import site.timecapsulearchive.core.global.security.oauth.dto.kakao.KakaoOAuth2UserInfo;


@Getter
public class OAuthAttributes {

    private final String authId;
    private final OAuth2UserInfo oauth2UserInfo;

    @Builder
    private OAuthAttributes(String authId, OAuth2UserInfo oauth2UserInfo) {
        this.authId = authId;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(
        final SocialType socialType,
        final String userNameAttributeName,
        final Map<String, Object> attributes
    ) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(
        final String userNameAttributeName,
        final Map<String, Object> attributes
    ) {
        final Long authId = (Long) attributes.get(userNameAttributeName);

        return OAuthAttributes.builder()
            .authId(String.valueOf(authId))
            .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
            .build();
    }

    public static OAuthAttributes ofGoogle(
        final String userNameAttributeName,
        final Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
            .authId((String) attributes.get(userNameAttributeName))
            .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
            .build();
    }
}
