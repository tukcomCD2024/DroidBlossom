package site.timecapsulearchive.core.common.config;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.auth.repository.BlackListCacheRepository;
import site.timecapsulearchive.core.domain.member.entity.Role;
import site.timecapsulearchive.core.global.api.limit.ApiLimitCheckInterceptor;
import site.timecapsulearchive.core.global.api.limit.ApiLimitProperties;
import site.timecapsulearchive.core.global.api.limit.ApiUsageCacheRepository;
import site.timecapsulearchive.core.global.security.filter.DefaultAuthenticationFilter;
import site.timecapsulearchive.core.global.security.jwt.JwtAuthenticationFilter;
import site.timecapsulearchive.core.global.security.jwt.JwtAuthenticationProvider;
import site.timecapsulearchive.core.global.security.property.DefaultKeyProperties;

@EnableWebSecurity
@TestConfiguration
public class TestMockMvcSecurityConfig {

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
            .authenticationProvider(jwtAuthenticationProvider())
            .addFilterBefore(testDefaultAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    private RequestMatcher notRequireAuthenticationMatcher() {
        return RequestMatchers.anyOf(antMatcher(HttpMethod.GET, "/pass"));
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        BlackListCacheRepository blackListCacheRepository = mock(BlackListCacheRepository.class);
        given(blackListCacheRepository.findBlackListTokenByMemberId(anyLong())).willReturn(null);

        return new JwtAuthenticationProvider(UnitTestDependency.jwtFactory(),
            blackListCacheRepository);
    }

    @Bean
    public DefaultKeyProperties testDefaultKeyProperties() {
        return new DefaultKeyProperties("testDefaultKey");
    }

    @Bean
    @Order(1)
    public DefaultAuthenticationFilter testDefaultAuthenticationFilter(
    ) {
        return new DefaultAuthenticationFilter(testDefaultKeyProperties());
    }

    @Bean
    @Order(2)
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authenticationManager(), new ObjectMapper(),
            notRequireAuthenticationMatcher());
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthenticationProvider());
    }

    @Bean
    public ApiLimitCheckInterceptor interceptor() {
        return new ApiLimitCheckInterceptor(apiLimitProperties(), repository());
    }

    @Bean
    public ApiLimitProperties apiLimitProperties() {
        return new ApiLimitProperties(10);
    }

    @Bean
    public ApiUsageCacheRepository repository() {
        return new ApiUsageCacheRepository(null);
    }
}
