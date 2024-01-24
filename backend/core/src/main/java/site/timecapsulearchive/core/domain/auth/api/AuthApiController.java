package site.timecapsulearchive.core.domain.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.dto.request.SignInRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.SignUpRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.TemporaryTokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.TokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.VerificationMessageSendRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.VerificationNumberValidRequest;
import site.timecapsulearchive.core.domain.auth.dto.response.OAuth2UriResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.VerificationMessageSendResponse;
import site.timecapsulearchive.core.domain.auth.service.MessageVerificationService;
import site.timecapsulearchive.core.domain.auth.service.TokenService;
import site.timecapsulearchive.core.domain.member.dto.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController implements AuthApi {

    private static final String KAKAO_AUTHORIZATION_ENDPOINT = "/auth/login/kakao";
    private static final String GOOGLE_AUTHORIZATION_ENDPOINT = "/auth/login/google";

    private final TokenService tokenService;
    private final MessageVerificationService messageVerificationService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public ResponseEntity<OAuth2UriResponse> getOAuth2KakaoUrl(HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString();
        String kakaoLoginUrl = baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + KAKAO_AUTHORIZATION_ENDPOINT
        );

        return ResponseEntity.ok(OAuth2UriResponse.from(kakaoLoginUrl));
    }

    @Override
    public ResponseEntity<OAuth2UriResponse> getOAuth2GoogleUrl(HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString();
        String googleLoginUrl = baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + GOOGLE_AUTHORIZATION_ENDPOINT
        );

        return ResponseEntity.ok(OAuth2UriResponse.from(googleLoginUrl));
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getTemporaryTokenResponseByKakao() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getTemporaryTokenResponseByGoogle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<ApiSpec<TemporaryTokenResponse>> reIssueTemporaryToken(
        @Valid @RequestBody final TemporaryTokenReIssueRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> reIssueAccessToken(
        @Valid @RequestBody final TokenReIssueRequest request
    ) {
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.reIssueToken(request.refreshToken())
            )
        );
    }

    @Override
    public ResponseEntity<ApiSpec<TemporaryTokenResponse>> signUpWithSocialProvider(
        @RequestBody final SignUpRequest request
    ) {
        Long id = memberService.createMember(memberMapper.signUpRequestToEntity(request));

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createTemporaryToken(id)
            )
        );
    }

    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> signInWithSocialProvider(
        @Valid @RequestBody final SignInRequest request) {
        Long memberId = memberService.findVerifiedMemberIdByAuthIdAndSocialType(request.authId(),
            request.socialType());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createNewToken(memberId)
            )
        );
    }

    @Override
    public ResponseEntity<ApiSpec<VerificationMessageSendResponse>> sendVerificationMessage(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final VerificationMessageSendRequest request
    ) {
        final VerificationMessageSendResponse response = messageVerificationService.sendVerificationMessage(
            memberId,
            request.receiver(),
            request.appHashKey()
        );

        return ResponseEntity.accepted()
            .body(
                ApiSpec.success(
                    SuccessCode.ACCEPTED,
                    response
                )
            );
    }

    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> validVerificationMessage(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final VerificationNumberValidRequest request
    ) {
        TokenResponse response = messageVerificationService.getRandomNickname(
            memberId,
            request.certificationNumber(),
            request.receiver()
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                response
            )
        );
    }
}