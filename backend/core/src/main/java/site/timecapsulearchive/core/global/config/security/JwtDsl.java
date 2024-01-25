package site.timecapsulearchive.core.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import site.timecapsulearchive.core.global.security.jwt.JwtAuthenticationFilter;

@RequiredArgsConstructor
public class JwtDsl extends AbstractHttpConfigurer<JwtDsl, HttpSecurity> {

    private final AuthenticationProvider jwtAuthenticationProvider;
    private final ObjectMapper objectMapper;
    private final RequestMatcher notRequireAuthenticationMatcher;

    public static JwtDsl jwtDsl(
        AuthenticationProvider authenticationProvider,
        ObjectMapper objectMapper,
        RequestMatcher requestMatcher
    ) {
        return new JwtDsl(
            authenticationProvider,
            objectMapper,
            requestMatcher
        );
    }

    @Override
    public void configure(HttpSecurity http) {
        http
            .authenticationProvider(jwtAuthenticationProvider)
            .addFilterBefore(
                jwtAuthenticationFilter(http.getSharedObject(AuthenticationManager.class)),
                UsernamePasswordAuthenticationFilter.class
            );
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter(
        AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(
            authenticationManager,
            objectMapper,
            notRequireAuthenticationMatcher
        );
    }
}
