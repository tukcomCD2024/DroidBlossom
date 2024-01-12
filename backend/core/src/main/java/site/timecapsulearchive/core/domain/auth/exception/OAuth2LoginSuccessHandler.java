package site.timecapsulearchive.core.domain.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import site.timecapsulearchive.core.domain.auth.dto.oauth.CustomOAuth2User;

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        //TODO : JWT 필요
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // Archive 처음 사용자 회원가입
            if (oAuth2User.isNotVerified()) {
                // 전화번호 인증 페이지로 리다이렉트
            } else {
                // Archive 사용자인 경우 accessToken과 refrehToken 발급
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
