package site.timecapsulearchive.core.global.config.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import site.timecapsulearchive.core.domain.member.entity.Role;
import site.timecapsulearchive.core.global.security.jwt.JwtAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider jwtAuthenticationProvider;
    private final ObjectMapper objectMapper;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChainWithJwt(final HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(header -> header.frameOptions(FrameOptionsConfig::disable))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
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
            .exceptionHandling(error -> error.accessDeniedHandler(accessDeniedHandler))
            .authenticationProvider(jwtAuthenticationProvider)
            .addFilterBefore(
                jwtAuthenticationFilter(http.getSharedObject(AuthenticationManager.class)),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter(
        final AuthenticationManager authenticationManager
    ) {
        return new JwtAuthenticationFilter(
            authenticationManager,
            objectMapper,
            notRequireAuthenticationMatcher()
        );
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
            antMatcher(HttpMethod.GET, "/actuator/**")
        );
    }
}


