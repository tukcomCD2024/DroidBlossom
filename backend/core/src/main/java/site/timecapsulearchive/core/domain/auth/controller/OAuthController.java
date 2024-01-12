package site.timecapsulearchive.core.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.domain.auth.api.AuthApi;
import site.timecapsulearchive.core.domain.auth.dto.oauth.google.GoogleOauthUri;
import site.timecapsulearchive.core.domain.auth.dto.oauth.kakao.KakaoOauthUri;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController implements AuthApi {

    private final GoogleOauthUri googleOauthUri;
    private final KakaoOauthUri kakaoOauthUri;

    @GetMapping("/kakao/authorize")
    public String redirectToKakaoAuthorization() {
        return kakaoOauthUri.getOauthRedirectURL();
    }

    @GetMapping("/google/authorize")
    public String redirectToGoogleAuthorization() {
        return googleOauthUri.getOauthRedirectURL();
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getOAuth2KakaoPage() {
        return null;
    }

    @Override
    public ResponseEntity<TemporaryTokenResponse> getOAuth2GooglePage() {
        return null;
    }

    @Override
    public ResponseEntity<TokenResponse> reIssueAccessToken() {
        return null;
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
