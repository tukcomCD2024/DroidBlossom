package site.timecapsulearchive.core.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.timecapsulearchive.core.global.security.jwt.JwtAuthenticationFilter;

@RequiredArgsConstructor
public class JwtDsl extends AbstractHttpConfigurer<JwtDsl, HttpSecurity> {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public static JwtDsl jwtDsl(
        AuthenticationConfiguration authenticationConfiguration,
        AuthenticationProvider authenticationProvider,
        AuthenticationFailureHandler authenticationEntryPoint
    ) {
        return new JwtDsl(
            authenticationConfiguration,
            authenticationProvider,
            authenticationEntryPoint
        );
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(jwtAuthenticationProvider)
            .addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            );
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(
            authenticationConfiguration.getAuthenticationManager(),
            authenticationFailureHandler
        );
    }
}
