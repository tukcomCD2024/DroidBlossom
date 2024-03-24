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
import site.timecapsulearchive.core.domain.auth.data.request.EmailSignInRequest;
import site.timecapsulearchive.core.domain.auth.data.request.EmailSignUpRequest;
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
import site.timecapsulearchive.core.domain.auth.service.MessageVerificationService;
import site.timecapsulearchive.core.domain.auth.service.TokenManager;
import site.timecapsulearchive.core.domain.member.data.mapper.MemberMapper;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.common.response.ApiSpec;
import site.timecapsulearchive.core.global.common.response.SuccessCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController implements AuthApi {

    private static final String KAKAO_AUTHORIZATION_ENDPOINT = "/auth/login/kakao";
    private static final String GOOGLE_AUTHORIZATION_ENDPOINT = "/auth/login/google";

    private final TokenManager tokenService;
    private final MessageVerificationService messageVerificationService;
    private final MemberService memberService;
    private final MemberMapper memberMapper;


    @GetMapping(value = "/login/url/kakao", produces = {"application/json"})
    @Override
    public ResponseEntity<OAuth2UriResponse> getOAuth2KakaoUrl(final HttpServletRequest request) {
        final String baseUrl = request.getRequestURL().toString();
        final String kakaoLoginUrl = baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + KAKAO_AUTHORIZATION_ENDPOINT
        );

        return ResponseEntity.ok(OAuth2UriResponse.from(kakaoLoginUrl));
    }

    @GetMapping(value = "/login/url/google", produces = {"application/json"})
    @Override
    public ResponseEntity<OAuth2UriResponse> getOAuth2GoogleUrl(final HttpServletRequest request) {
        final String baseUrl = request.getRequestURL().toString();
        final String googleLoginUrl = baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + GOOGLE_AUTHORIZATION_ENDPOINT
        );

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
        final Long id = memberService.findNotVerifiedMemberIdByAuthIdAndSocialType(
            request.authId(), request.socialType()
        );

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createTemporaryToken(id)
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
        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.reIssueToken(request.refreshToken())
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
        final Long id = memberService.createMember(memberMapper.signUpRequestToDto(request));

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createTemporaryToken(id)
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
        final Long memberId = memberService.findVerifiedMemberIdByAuthIdAndSocialType(
            request.authId(), request.socialType());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createNewToken(memberId)
            )
        );
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
        final TokenResponse response = messageVerificationService.validVerificationMessage(
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

    @PostMapping(
        value = "/sign-up/email",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TemporaryTokenResponse>> signUpWithEmail(
        @Valid @RequestBody final EmailSignUpRequest request
    ) {
        final Long id = memberService.createMemberWithEmailAndPassword(request.email(), request.password());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createTemporaryToken(id)
            )
        );
    }

    @PostMapping(
        value = "/sign-in/email",
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    @Override
    public ResponseEntity<ApiSpec<TokenResponse>> signInWithEmail(
        @Valid @RequestBody final EmailSignInRequest request
    ) {
        final Long id = memberService.findVerifiedMemberIdByEmailAndPassword(request.email(),
            request.password());

        return ResponseEntity.ok(
            ApiSpec.success(
                SuccessCode.SUCCESS,
                tokenService.createNewToken(id)
            )
        );
    }
}