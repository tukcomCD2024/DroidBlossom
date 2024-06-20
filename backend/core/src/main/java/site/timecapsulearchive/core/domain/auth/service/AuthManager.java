package site.timecapsulearchive.core.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private static final String KAKAO_AUTHORIZATION_ENDPOINT = "/auth/login/kakao";
    private static final String GOOGLE_AUTHORIZATION_ENDPOINT = "/auth/login/google";

    public String getOAuth2KakaoUrl(final HttpServletRequest request) {
        final String baseUrl = request.getRequestURL().toString();

        return baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + KAKAO_AUTHORIZATION_ENDPOINT
        );
    }

    public String getOauth2GoogleUrl(HttpServletRequest request) {
        final String baseUrl = request.getRequestURL().toString();

        return baseUrl.replace(
            request.getRequestURI(),
            request.getContextPath() + GOOGLE_AUTHORIZATION_ENDPOINT
        );
    }
}
