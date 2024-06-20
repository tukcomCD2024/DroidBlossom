package site.timecapsulearchive.core.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.data.dto.VerificationMessageSendDto;
import site.timecapsulearchive.core.domain.auth.data.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.data.response.TokenResponse;
import site.timecapsulearchive.core.domain.member.data.dto.SignUpRequestDto;
import site.timecapsulearchive.core.domain.member.entity.SocialType;
import site.timecapsulearchive.core.domain.member.service.MemberService;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private static final String KAKAO_AUTHORIZATION_ENDPOINT = "/auth/login/kakao";
    private static final String GOOGLE_AUTHORIZATION_ENDPOINT = "/auth/login/google";

    private final TokenManager tokenManager;
    private final MemberService memberService;
    private final MessageVerificationService messageVerificationService;

    public String getOAuth2KakaoUrl(final HttpServletRequest request) {
        final String baseUrl = request.getRequestURL().toString();

        return baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + KAKAO_AUTHORIZATION_ENDPOINT
        );
    }

    public String getOauth2GoogleUrl(final HttpServletRequest request) {
        final String baseUrl = request.getRequestURL().toString();

        return baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + GOOGLE_AUTHORIZATION_ENDPOINT
        );
    }

    public TemporaryTokenResponse reIssueTemporaryToken(final String authId,
        final SocialType socialType) {
        final Long notVerifiedMemberId = memberService.findNotVerifiedMemberIdBy(authId,
            socialType);

        return tokenManager.createTemporaryToken(notVerifiedMemberId);
    }

    public TokenResponse reIssueToken(final String refreshToken) {
        return tokenManager.reIssueToken(refreshToken);
    }

    public TemporaryTokenResponse signUp(final SignUpRequestDto dto) {
        final Long createdMemberId = memberService.createMember(dto);

        return tokenManager.createTemporaryToken(createdMemberId);
    }

    public TokenResponse signIn(final String authId, final SocialType socialType) {
        final Long verifiedSocialMemberId = memberService.findVerifiedSocialMemberIdBy(authId,
            socialType);

        return tokenManager.createNewToken(verifiedSocialMemberId);
    }

    public VerificationMessageSendDto sendVerificationMessage(
        final Long memberId,
        final String receiver,
        final String appHashKey
    ) {
        return messageVerificationService.sendVerificationMessage(memberId, receiver, appHashKey);
    }
}
