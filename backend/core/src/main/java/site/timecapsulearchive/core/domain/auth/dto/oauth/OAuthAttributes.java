package site.timecapsulearchive.core.domain.auth.dto.oauth;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oauth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
        String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
            .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
            .build();
    }
}
