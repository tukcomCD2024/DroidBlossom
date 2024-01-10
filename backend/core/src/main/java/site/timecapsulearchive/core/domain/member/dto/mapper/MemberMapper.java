package site.timecapsulearchive.core.domain.member.dto.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.dto.oauth.OAuth2UserInfo;
import site.timecapsulearchive.core.domain.auth.entity.SocialType;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Component
public class MemberMapper {

    public Member OAuthToEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return Member.builder()
            .socialType(socialType)
            .email(oAuth2UserInfo.getEmail())
            .profileUrl(oAuth2UserInfo.getImageUrl())
            .build();
    }
}