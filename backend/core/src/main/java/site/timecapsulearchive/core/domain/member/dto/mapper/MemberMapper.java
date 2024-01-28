package site.timecapsulearchive.core.domain.member.dto.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.dto.request.SignUpRequest;
import site.timecapsulearchive.core.domain.member.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.oauth.dto.OAuth2UserInfo;

@Component
public class MemberMapper {

    public Member OAuthToEntity(
        String authId,
        SocialType socialType,
        OAuth2UserInfo oAuth2UserInfo
    ) {
        return Member.builder()
            .authId(authId)
            .socialType(socialType)
            .email(oAuth2UserInfo.getEmail())
            .profileUrl(oAuth2UserInfo.getImageUrl())
            .build();
    }

    public SignUpRequestDto signUpRequestToDto(SignUpRequest request) {
        return new SignUpRequestDto(
            request.authId(),
            request.email(),
            request.profileUrl(),
            request.socialType()
        );
    }

    public Member signUpRequestDtoToEntity(SignUpRequestDto dto) {
        return Member.builder()
            .authId(dto.authId())
            .email(dto.email())
            .profileUrl(dto.profileUrl())
            .socialType(dto.socialType())
            .build();
    }
}