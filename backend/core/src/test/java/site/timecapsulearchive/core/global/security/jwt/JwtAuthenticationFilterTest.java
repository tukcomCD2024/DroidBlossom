package site.timecapsulearchive.core.global.security.jwt;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.core.common.config.TestMockMvcSecurityConfig;
import site.timecapsulearchive.core.common.controller.AuthTestController;
import site.timecapsulearchive.core.common.fixture.security.TokenFixture;

@WebMvcTest(
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestController.class),
    }
)
@Import(value = TestMockMvcSecurityConfig.class)
@DisplayName("jwt_인증_필터_테스트")
class JwtAuthenticationFilterTest {

    private static final Long MEMBER_ID = 1L;
    private static final String TOKEN_TYPE = "Bearer ";
    private static final String NOT_EXIST_PASS_URL = "/pass";
    private static final String AUTHENTICATION_ERROR_CODE = "AUTH-003";

    private MockMvc mockMvc;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new AuthTestController())
            .apply(springSecurity(jwtAuthenticationFilter))
            .build();
    }

    @Test
    void 올바른_액세스_토큰으로_인증하면_ok가_반환된다() throws Exception {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);

        //when
        ResultActions result = mockMvc
            .perform(
                get("/not-pass")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        TOKEN_TYPE + accessToken
                    )
            );

        //then
        result.andExpect(status().isOk());
    }

    @Test
    void 변조된_액세스_토큰으로_인증하면_에러_코드가_반환된다() throws Exception {
        //given
        String accessToken = TokenFixture.forgedSignature(MEMBER_ID, TokenType.ACCESS);

        //when
        ResultActions result = mockMvc
            .perform(
                get("/not-pass")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        TOKEN_TYPE + accessToken
                    )
            );

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(AUTHENTICATION_ERROR_CODE));
    }

    @Test
    void Bearer_prefix_없이_액세스_토큰만으로_인증하면_에러_코드가_반환된다() throws Exception {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);

        //when
        ResultActions result = mockMvc
            .perform(
                get("/not-pass")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        accessToken
                    )
            );

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(AUTHENTICATION_ERROR_CODE));
    }

    @Test
    void 유효하지_않은_액세스_토큰으로_인증하면_에러_코드가_반환된다() throws Exception {
        //given
        String invalidAccessToken = TokenFixture.invalidToken();

        //when
        ResultActions result = mockMvc.perform(
            get("/not-pass")
                .header(HttpHeaders.AUTHORIZATION, invalidAccessToken)
        );

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(AUTHENTICATION_ERROR_CODE));
    }

    @Test
    void 액세스_토큰_없이_인증하면_에러코드가_반환된다() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(get("/not-pass"));

        //then
        result.andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(AUTHENTICATION_ERROR_CODE));
    }

    @Test
    void 인증이_통과되는_엔드포인트로_요청하면_ok가_반환된다() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(get(NOT_EXIST_PASS_URL));

        //then
        result.andExpect(status().isOk());
    }
}