package site.timecapsulearchive.core.domain.member.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.data.request.SignUpRequest;
import site.timecapsulearchive.core.domain.member.data.dto.MemberDetailResponseDto;
import site.timecapsulearchive.core.domain.member.data.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.data.response.MemberDetailResponse;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.global.security.oauth.dto.OAuth2UserInfo;

@Component
public class MemberMapper {

    public Member OAuthToEntity(
        final String authId,
        final SocialType socialType,
        final OAuth2UserInfo oAuth2UserInfo
    ) {
        return Member.builder()
            .authId(authId)
            .socialType(socialType)
            .email(oAuth2UserInfo.getEmail())
            .profileUrl(oAuth2UserInfo.getImageUrl())
            .build();
    }

    public SignUpRequestDto signUpRequestToDto(final SignUpRequest request) {
        return new SignUpRequestDto(
            request.authId(),
            request.email(),
            request.profileUrl(),
            request.socialType()
        );
    }

    public Member signUpRequestDtoToEntity(final SignUpRequestDto dto) {
        return Member.builder()
            .authId(dto.authId())
            .email(dto.email())
            .profileUrl(dto.profileUrl())
            .socialType(dto.socialType())
            .build();
    }

    public MemberDetailResponse memberDetailResponseDtoToResponse(
        final MemberDetailResponseDto dto,
        final String decryptedPhone
    ) {
        return new MemberDetailResponse(dto.nickname(), dto.profileUrl(), decryptedPhone);
    }
}