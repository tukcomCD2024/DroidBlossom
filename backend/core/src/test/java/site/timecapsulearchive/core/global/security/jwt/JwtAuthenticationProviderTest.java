package site.timecapsulearchive.core.global.security.jwt;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import site.timecapsulearchive.core.global.config.security.JwtProperties;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@DisplayName("jwt_인증_프로바이더_테스트")
class JwtAuthenticationProviderTest {

    private static final Long MEMBER_ID = 1L;

    private final JwtFactory jwtFactory;

    private final JwtFactory inValidJwtFactory;

    private final AuthenticationProvider jwtAuthenticationProvider;

    public JwtAuthenticationProviderTest() {
        this.jwtFactory = createJwtFactory();
        this.inValidJwtFactory = createInValidJwtFactory();
        this.jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtFactory);
    }

    private JwtFactory createJwtFactory() {
        return new JwtFactory(new JwtProperties("a".repeat(32), 3600, 3600, 3600));
    }

    private JwtFactory createInValidJwtFactory() {
        return new JwtFactory(new JwtProperties("b".repeat(32), 0, 0, 0));
    }

    @Test
    @DisplayName("유효한_액세스_토큰으로_인증_테스트")
    void authenticateWithValidAccessTokenTest() {
        //given
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);
        JwtAuthenticationToken auth = JwtAuthenticationToken.unauthenticated(accessToken);

        //when
        Authentication result = jwtAuthenticationProvider.authenticate(auth);

        //then
        assertSoftly(softly -> {
            softly.assertThat(result).isNotNull();
            softly.assertThat(result.isAuthenticated()).isTrue();
            softly.assertThat(result.getPrincipal()).isEqualTo(MEMBER_ID);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "trash", "Bearer "})
    @DisplayName("유효하지_않은_액세스_토큰으로_인증_테스트")
    void authenticateWithInValidAccessTokenTest(String accessToken) {
        //given
        JwtAuthenticationToken auth = JwtAuthenticationToken.unauthenticated(accessToken);

        //when
        //then
        assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(auth))
            .isExactlyInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("위변조된_액세스_토큰으로_인증_테스트")
    void authenticateWithForgeryAccessTokenTest() {
        //given
        String accessToken = inValidJwtFactory.createAccessToken(MEMBER_ID);
        JwtAuthenticationToken auth = JwtAuthenticationToken.unauthenticated(accessToken);

        //when
        //then
        assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(auth))
            .isExactlyInstanceOf(InvalidTokenException.class);
    }
}