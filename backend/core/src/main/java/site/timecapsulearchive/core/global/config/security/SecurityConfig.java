package site.timecapsulearchive.core.global.config.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import site.timecapsulearchive.core.domain.member.entity.Role;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOauth2UserService;
    private final AuthenticationSuccessHandler oAuth2LoginSuccessHandler;

    private final AuthenticationFailureHandler oauth2LoginFailureHandler;
    private final AuthenticationProvider jwtAuthenticationProvider;
    private final ObjectMapper objectMapper;

    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainWithJwt(final HttpSecurity http) throws Exception {
        http.apply(
            CommonSecurityDsl.commonSecurityDsl()
        );

        http
            .securityMatchers(
                c -> c.requestMatchers(new NegatedRequestMatcher(antMatcher("/auth/login/**")))
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(notRequireAuthenticationMatcher()).permitAll()
                .requestMatchers(
                    "/auth/verification/**",
                    "/temporary-token/re-issue"
                ).hasRole(Role.TEMPORARY.name())
                .anyRequest().hasRole(Role.USER.name())
            )
            .exceptionHandling(error -> error.accessDeniedHandler(accessDeniedHandler));

        http.apply(
            JwtDsl.jwtDsl(
                jwtAuthenticationProvider,
                objectMapper,
                notRequireAuthenticationMatcher()
            )
        );

        return http.build();
    }

    private RequestMatcher notRequireAuthenticationMatcher() {
        return RequestMatchers.anyOf(
            antMatcher("/v3/api-docs/**"),
            antMatcher("/swagger-ui/**"),
            antMatcher(HttpMethod.POST, "/auth/token/re-issue"),
            antMatcher(HttpMethod.POST, "/me/status"),
            antMatcher(HttpMethod.POST, "/auth/sign-up"),
            antMatcher(HttpMethod.POST, "/auth/sign-in"),
            antMatcher(HttpMethod.GET, "/auth/login/**"),
            antMatcher(HttpMethod.GET, "/health"),
            antMatcher(HttpMethod.POST, "/auth/temporary-token/re-issue"),
            antMatcher(HttpMethod.POST, "/auth/sign-up/email"),
            antMatcher(HttpMethod.POST, "/auth/sign-in/email"),
            antMatcher(HttpMethod.POST, "/me/check-duplication/email"),
            antMatcher(HttpMethod.GET, "/actuator")
        );
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainWithOAuth(final HttpSecurity http) throws Exception {
        http.apply(
            CommonSecurityDsl.commonSecurityDsl()
        );

        http
            .securityMatcher("/auth/login/**")
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        http.apply(
            OAuthDsl.oauthDsl(
                customOauth2UserService,
                oAuth2LoginSuccessHandler,
                oauth2LoginFailureHandler
            )
        );

        return http.build();
    }
}


