package site.timecapsulearchive.core.global.config.security;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOauth2UserService;
    private final AuthenticationSuccessHandler oAuth2LoginSuccessHandler;

    @Qualifier("oauth2LoginFailureHandler")
    private final AuthenticationFailureHandler oauth2LoginFailureHandler;

    @Qualifier("jwtAuthenticationFailureHandler")
    private final AuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final AuthenticationProvider jwtAuthenticationProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**");
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainWithOAuth(HttpSecurity http) throws Exception {
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

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainWithJwt(
        HttpSecurity http,
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        http.apply(
            CommonSecurityDsl.commonSecurityDsl()
        );

        http
            .securityMatcher(regexMatcher("^\\/(?!auth\\/login).*"))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            );

        http.apply(
            JwtDsl.jwtDsl(
                authenticationConfiguration,
                jwtAuthenticationProvider,
                jwtAuthenticationFailureHandler
            )
        );

        return http.build();
    }
}


