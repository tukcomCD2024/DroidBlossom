package site.timecapsulearchive.core.domain.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.data.dto.TemporaryTokenDto;
import site.timecapsulearchive.core.domain.auth.data.dto.TokenDto;
import site.timecapsulearchive.core.domain.auth.data.dto.VerificationMessageSendDto;
import site.timecapsulearchive.core.domain.auth.data.request.SignInRequest;
import site.timecapsulearchive.core.domain.auth.data.request.SignUpRequest;
import site.timecapsulearchive.core.domain.auth.data.request.TemporaryTokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.data.request.TokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.data.request.VerificationMessageSendRequest;
import site.timecapsulearchive.core.domain.auth.data.request.VerificationNumberValidRequest;
import site.timecapsulearchive.core.domain.auth.data.response.OAuth2UriResponse;
import site.timecapsulearchive.core.domain.auth.data.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.data.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.data.response.VerificationMessageSendResponse;
import site.timecapsulearchive.core.domain.auth.service.AuthManager;
import site.timecapsulearchive.core.global.common.argument.AccessToken;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController implements AuthApi {

    private final AuthManager authManager;

    @GetMapping(value = "/login/url/kakao", produces = {"application/json"})
    @Override
    public ResponseEntity<OAuth2UriResponse> getOAuth2KakaoUrl(final HttpServletRequest request) {
        final String kakaoLoginUrl = authManager.getOAuth2KakaoUrl(request);

        return ResponseEntity.ok(OAuth2UriResponse.from(kakaoLoginUrl));
    }

    @GetMapping(value = "/login/url/google", produces = {"application/json"})
    @Override
    public ResponseEntity<OAuth2UriResponse> getOAuth2GoogleUrl(final HttpServletRequest request) {
        final String googleLoginUrl = authManager.getOauth2GoogleUrl(request);

        return ResponseEntity.ok(OAuth2UriResponse.from(googleLoginUrl));
    }

    @GetMapping(value = "/login/oauth2/code/kakao", produces = {"application/json"})
    @Override
    public ResponseEntity<TemporaryTokenResponse> getTemporaryTokenByKakao() {
        throw new UnsupportedOperationException();
    }

    @GetMapping(value = "/login/oauth2/code/google", produces = {"application/json"})
    @Override
    public ResponseEntity<TemporaryTokenResponse> getTemporaryTokenByGoogle() {
        throw new UnsupportedOperationException();
    }

    @PostMapping(
        value = "/temporary-token/re-issue",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TemporaryTokenResponse>> reIssueTemporaryToken(
        @Valid @RequestBody final TemporaryTokenReIssueRequest request
    ) {
        final TemporaryTokenDto temporaryToken = authManager.reIssueTemporaryToken(
            request.authId(), request.socialType());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                temporaryToken.toResponse()
            )
        );
    }

    @PostMapping(
        value = "/token/re-issue",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> reIssueAccessToken(
        @Valid @RequestBody final TokenReIssueRequest request
    ) {
        final TokenDto token = authManager.reIssueToken(request.refreshToken());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                token.toResponse()
            )
        );
    }

    @PostMapping(
        value = "/sign-up",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TemporaryTokenResponse>> signUpWithSocialProvider(
        @Valid @RequestBody final SignUpRequest request
    ) {
        final TemporaryTokenDto temporaryToken = authManager.signUp(request.toDto());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                temporaryToken.toResponse()
            )
        );
    }

    @PostMapping(
        value = "/sign-in",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> signInWithSocialProvider(
        @Valid @RequestBody final SignInRequest request) {
        final TokenDto token = authManager.signIn(request.authId(), request.socialType());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                token.toResponse()
            )
        );
    }

    @PostMapping(
        value = "/sign-out",
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<String>> signOutWithSocialProvider(
        @AuthenticationPrincipal final Long memberId,
        @AccessToken final String accessToken
    ) {
        authManager.signOut(memberId, accessToken);

        return ResponseEntity.ok(ApiSpec.empty(SuccessCode.SUCCESS));
    }

    @PostMapping(
        value = "/verification/send-message",
        consumes = {"application/json"},
        produces = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<VerificationMessageSendResponse>> sendVerificationMessage(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final VerificationMessageSendRequest request
    ) {
        final VerificationMessageSendDto verificationMessageSendDto = authManager.sendVerificationMessage(
            memberId, request.receiver(), request.appHashKey());

        return ResponseEntity.accepted()
            .body(
                ApiSpec.success(
                    SuccessCode.ACCEPTED,
                    verificationMessageSendDto.toResponse()
                )
            );
    }

    @PostMapping(
        value = "/verification/valid-message",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> validVerificationMessage(
        @AuthenticationPrincipal final Long memberId,
        @Valid @RequestBody final VerificationNumberValidRequest request
    ) {
        TokenDto token = authManager.validVerificationMessage(memberId,
            request.certificationNumber(), request.receiver());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                token.toResponse()
            )
        );
    }
}