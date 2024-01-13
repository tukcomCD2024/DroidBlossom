package site.timecapsulearchive.core.global.security.jwt;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import site.timecapsulearchive.core.global.config.security.JwtProperties;

/**
 * JwtAuthenticationFilter 테스트 여러 가지 토큰 값(유효하지 않은, 비어있는, 공백)으로 Authentication Filter가 동작하는지 확인하는 테스트
 * 코드이다.
 */
@WebMvcTest(
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestController.class)
    }
)
@DisplayName("jwt_인증_필터_테스트")
class JwtAuthenticationFilterTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtFactory jwtFactory;

    @Autowired
    private JwtFactory inValidJwtFactory;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(jwtAuthenticationFilter))
            .build();
    }

    @Test
    @DisplayName("올바른_액세스_토큰으로_인증_테스트")
    void authenticateWithValidJwtTest() throws Exception {
        //given
        String accessToken = jwtFactory.createAccessToken(1L);

        //when
        ResultActions result = mockMvc
            .perform(
                post("/auth/test")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        JwtConstants.TOKEN_TYPE.getValue() + accessToken
                    )
            );

        //then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("위변조된_액세스_토큰으로_인증_테스트")
    void authenticateWithNotValidJwtTest() throws Exception {
        //given
        JwtFactory notValidJwtFactory = inValidJwtFactory;
        String accessToken = notValidJwtFactory.createAccessToken(1L);

        //when
        ResultActions result = mockMvc
            .perform(
                post("/api/test")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        JwtConstants.TOKEN_TYPE.getValue() + accessToken
                    )
            );

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("J001"));
    }

    @Test
    @DisplayName("Bearer_prefix_없이_액세스_토큰만으로_인증_테스트")
    void authenticateWithoutBearerPrefixTest() throws Exception {
        //given
        String accessToken = jwtFactory.createAccessToken(1L);

        //when
        ResultActions result = mockMvc
            .perform(
                post("/api/test")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        accessToken
                    )
            );

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("J001"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Bearer ", "trash"})
    @DisplayName("유효하지_않은_액세스_토큰으로_인증_테스트")
    void authenticateWithInValidValueTest(String accessToken) throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
            post("/api/test")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
        );

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("J001"));
    }

    @Test
    @DisplayName("액세스_토큰_없이_인증_테스트")
    void authenticateWithEmptyValue() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(post("/api/test"));

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value("J001"));
    }

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public JwtFactory jwtFactory() {
            return new JwtFactory(new JwtProperties("J".repeat(32), 3600, 3600, 3600));
        }

        @Bean
        public JwtFactory inValidJwtFactory() {
            return new JwtFactory(new JwtProperties("B".repeat(32), 3600, 3600, 3600));
        }

        @Bean
        public AuthenticationFailureHandler jwtAuthenticationFailureHandler() {
            return new JwtAuthenticationFailureHandler(new ObjectMapper());
        }

        @Bean
        public JwtAuthenticationProvider jwtAuthenticationProvider() {
            return new JwtAuthenticationProvider(jwtFactory());
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter(authenticationManager(),
                jwtAuthenticationFailureHandler());
        }

        @Bean
        public AuthenticationManager authenticationManager() {
            return new ProviderManager(jwtAuthenticationProvider());
        }
    }
}