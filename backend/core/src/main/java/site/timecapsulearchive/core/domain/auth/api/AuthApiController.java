package site.timecapsulearchive.core.domain.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.dto.request.TokenReIssueRequest;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.service.TokenService;

@RequestMapping("/")
@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {

    private final TokenService tokenService;

    @Override
    public ResponseEntity<TemporaryTokenResponse> getOAuth2KakaoPage() {
        return null;
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getOAuth2GooglePage() {
        return null;
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
