package site.timecapsulearchive.core.global.security.oauth.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.oauth.dto.google.GoogleOAuth2UserInfo;
import site.timecapsulearchive.core.global.security.oauth.dto.kakao.KakaoOAuth2UserInfo;
import site.timecapsulearchive.core.global.util.TagGenerator;
import site.timecapsulearchive.core.global.util.nickname.MakeRandomNickNameUtil;


@Getter
public class OAuthAttributes {

    private final String authId;
    private final OAuth2UserInfo OAuth2UserInfo;

    @Builder
    private OAuthAttributes(String authId, OAuth2UserInfo OAuth2UserInfo) {
        this.authId = authId;
        this.OAuth2UserInfo = OAuth2UserInfo;
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
            .OAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
            .build();
    }

    public static OAuthAttributes ofGoogle(
        final String userNameAttributeName,
        final Map<String, Object> attributes
    ) {
        return OAuthAttributes.builder()
            .authId((String) attributes.get(userNameAttributeName))
            .OAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
            .build();
    }

    public Member OAuthToMember(
        final SocialType socialType
    ) {
        return Member.builder()
            .authId(authId)
            .nickname(MakeRandomNickNameUtil.makeRandomNickName())
            .email(OAuth2UserInfo.getEmail())
            .profileUrl(OAuth2UserInfo.getImageUrl())
            .socialType(socialType)
            .tag(TagGenerator.generate(OAuth2UserInfo.getEmail(), socialType))
            .build();
    }
}
