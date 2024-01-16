package site.timecapsulearchive.core.domain.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.dto.request.SignInRequest;
import site.timecapsulearchive.core.domain.auth.dto.request.TokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.dto.response.OAuthUrlResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.service.TokenService;
import site.timecapsulearchive.core.domain.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController implements AuthApi {

    private final TokenService tokenService;
    private final MemberService memberService;

    @Override
    public ResponseEntity<OAuthUrlResponse> getOAuth2KakaoUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<OAuthUrlResponse> getOAuth2GoogleUrl() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getTemporaryTokenResponseByKakao() {
        return null;
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getTemporaryTokenResponseByGoogle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<TokenResponse> reIssueAccessToken(
        @RequestBody final TokenReIssueRequest request) {
        return ResponseEntity.ok(tokenService.reIssueToken(request.refreshToken()));
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> signInWithSocialProvider(
        @RequestBody final SignInRequest request) {
        Long id = memberService.createUser(request.toEntity());

        return ResponseEntity.ok(tokenService.createTemporaryToken(id));
    }

    @Override
    public ResponseEntity<Void> sendVerificationMessage() {
        return null;
    }

    @Override
    public ResponseEntity<Void> validVerificationMessage() {
        return null;
    }
}