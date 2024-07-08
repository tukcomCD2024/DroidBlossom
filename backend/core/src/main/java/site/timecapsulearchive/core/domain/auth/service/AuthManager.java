package site.timecapsulearchive.core.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.data.dto.TemporaryTokenDto;
import site.timecapsulearchive.core.domain.auth.data.dto.TokenDto;
import site.timecapsulearchive.core.domain.auth.data.dto.VerificationMessageSendDto;
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

    public TemporaryTokenDto reIssueTemporaryToken(final String authId,
        final SocialType socialType) {
        final Long notVerifiedMemberId = memberService.findNotVerifiedMemberIdBy(authId,
            socialType);

        return tokenManager.createTemporaryToken(notVerifiedMemberId);
    }

    public TokenDto reIssueToken(final String refreshToken) {
        return tokenManager.reIssueToken(refreshToken);
    }

    public TemporaryTokenDto signUp(final SignUpRequestDto dto) {
        final Long createdMemberId = memberService.createMember(dto);

        return tokenManager.createTemporaryToken(createdMemberId);
    }

    public TokenDto signIn(final String authId, final SocialType socialType) {
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

    public TokenDto validVerificationMessage(
        final Long memberId,
        final String certificationNumber,
        final String receiver
    ) {
        final byte[] plain = receiver.getBytes(StandardCharsets.UTF_8);

        messageVerificationService.validVerificationMessage(memberId,
            certificationNumber, plain);

        Long verifiedMemberId = memberService.updateVerifiedMember(memberId, plain);

        return tokenManager.createNewToken(verifiedMemberId);
    }

    public void signOut(Long memberId, String accessToken) {
        tokenManager.removeRefreshToken(memberId);

        tokenManager.addBlackList(memberId, accessToken);
    }
}
