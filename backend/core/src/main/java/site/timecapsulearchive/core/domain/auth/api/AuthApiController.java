package site.timecapsulearchive.core.domain.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.dto.request.TokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.dto.response.OAuthUrlResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.service.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthApiController implements AuthApi {

    private final TokenService tokenService;

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
    public ResponseEntity<Void> sendVerificationMessage() {
        return null;
    }

    @Override
    public ResponseEntity<Void> validVerificationMessage() {
        return null;
    }
}