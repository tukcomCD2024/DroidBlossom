package site.timecapsulearchive.core.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.common.response.ErrorCode;
import site.timecapsulearchive.core.global.common.response.ErrorResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        SecurityContextHolder.clearContext();

        ErrorResponse errorResponse = ErrorResponse.create(
            ErrorCode.INVALID_TOKEN_EXCEPTION.getCode(),
            exception.getMessage()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter()
            .write(objectMapper.writeValueAsString(errorResponse));
    }
}
