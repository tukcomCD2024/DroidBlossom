package site.timecapsulearchive.core.domain.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.dto.oauth.CustomOAuth2User;
import site.timecapsulearchive.core.domain.auth.service.TokenService;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.ErrorResponse;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            PrintWriter writer = response.getWriter();

            if (oAuth2User.isNotVerified()) {
                writer.write(objectMapper.writeValueAsString(
                    tokenService.createTemporaryToken(oAuth2User.getId())
                ));
                return;
            }

            writer.write(objectMapper.writeValueAsString(
                tokenService.createNewToken(oAuth2User.getId())
            ));

        } catch (Exception exception) {
            log.info("oauth2 인증 실패", exception);

            ErrorResponse errorResponse = ErrorResponse.create(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                exception.getMessage()
            );

            response.getWriter()
                .write(objectMapper.writeValueAsString(errorResponse));
        }
    }
}
