package site.timecapsulearchive.core.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.ErrorResponse;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final int PREFIX_LENGTH = 7;
    private static final String TOKEN_TYPE = JwtConstants.TOKEN_TYPE.getValue();

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final RequestMatcher notRequireAuthenticationMatcher;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (notRequiresAuthentication(request)) {
            filterChain.doFilter(request, response);
        } else {
            String accessToken = extractAccessToken(request);

            try {
                if (accessToken.isBlank()) {
                    throw new InvalidTokenException();
                }

                Authentication authenticationResult = attemptAuthentication(accessToken);

                successfulAuthentication(authenticationResult);

                filterChain.doFilter(request, response);
            } catch (InvalidTokenException exception) {
                unsuccessfulAuthentication(
                    response,
                    exception
                );
            }
        }
    }

    private boolean notRequiresAuthentication(HttpServletRequest request) {
        return notRequireAuthenticationMatcher.matches(request);
    }


    private String extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotValidFormat(token)) {
            return "";
        }

        return token.substring(PREFIX_LENGTH);
    }

    private boolean isNotValidFormat(String token) {
        return !StringUtils.hasText(token) || !token.startsWith(TOKEN_TYPE);
    }

    private Authentication attemptAuthentication(String accessToken) {
        Authentication authentication = JwtAuthenticationToken.unauthenticated(accessToken);

        return authenticationManager.authenticate(authentication);
    }

    private void successfulAuthentication(Authentication authResult) {
        SecurityContextHolder.getContext()
            .setAuthentication(authResult);
    }

    private void unsuccessfulAuthentication(
        HttpServletResponse response,
        InvalidTokenException exception
    ) throws IOException {
        log.info("액세스 토큰 인증 실패", exception);
        SecurityContextHolder.clearContext();

        ErrorResponse errorResponse = ErrorResponse.create(
            ErrorCode.AUTHENTICATION_ERROR
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter()
            .write(objectMapper.writeValueAsString(errorResponse));
    }
}
