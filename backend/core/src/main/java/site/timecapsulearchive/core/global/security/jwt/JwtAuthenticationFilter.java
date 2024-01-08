package site.timecapsulearchive.core.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final int PREFIX_LENGTH = 7;
    private static final String AUTHORIZATION_HEADER = JwtConstants.TOKEN_TYPE.getValue();
    private static final String TOKEN_TYPE = JwtConstants.TOKEN_TYPE.getValue();

    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler jwtAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        try {
            if (accessToken.isBlank()) {
                throw new InvalidTokenException();
            }

            Authentication authenticationResult = attemptAuthentication(accessToken);

            successfulAuthentication(authenticationResult);

            filterChain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            unsuccessfulAuthentication(
                request, response,
                authenticationException
            );
        }
    }

    private String extractAccessToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

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
        HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authenticationException
    ) throws ServletException, IOException {
        jwtAuthenticationFailureHandler.onAuthenticationFailure(
            request,
            response,
            authenticationException
        );
    }
}
