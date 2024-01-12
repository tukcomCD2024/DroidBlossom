package site.timecapsulearchive.core.global.config.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import site.timecapsulearchive.core.domain.auth.service.CustomOAuth2UserService;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//    private final OAuth2LoginFailureHandler auth2LoginFailureHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChainWithJwt(
        HttpSecurity http,
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(header -> header.frameOptions(FrameOptionsConfig::disable))
            .securityMatcher("/**")
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**")
                .permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2login ->
                    oauth2login
                        .userInfoEndpoint(
                            userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                                customOauth2UserService)
                        )
//                    .successHandler(oAuth2LoginSuccessHandler)
//                    .failureHandler(auth2LoginFailureHandler)

            )
            .apply(
                JwtDsl.jwtDsl(
                    authenticationConfiguration,
                    jwtAuthenticationProvider,
                    authenticationFailureHandler
                )
            );

        return http.build();
    }
}


