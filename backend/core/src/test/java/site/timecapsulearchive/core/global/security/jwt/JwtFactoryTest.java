package site.timecapsulearchive.core.global.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import site.timecapsulearchive.core.global.config.security.JwtProperties;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

/**
 * JwtFactory가 올바르게 동작하는지 유효한 값과 쓰레기 값 또는 다른 JwtFactory에서 생성한 토큰을 바탕으로 확인한다.
 */
@DisplayName("jwt_팩토리_테스트")
class JwtFactoryTest {

    private static final String MEMBER_INFO_KEY = "memberInfo:" + UUID.randomUUID();
    private static final Long MEMBER_ID = 1L;
    private static final String TRASH = "trash";

    private final JwtFactory jwtFactory;
    private final JwtFactory notValidJwtFactory;

    public JwtFactoryTest() {
        this.jwtFactory = createJwtFactory();
        this.notValidJwtFactory = createInValidJwtFactory();
    }

    private JwtFactory createJwtFactory() {
        return new JwtFactory(new JwtProperties("a".repeat(32), 3600, 3600, 3600));
    }

    private JwtFactory createInValidJwtFactory() {
        return new JwtFactory(new JwtProperties("b".repeat(32), 3600, 3600, 3600));
    }

    @Test
    @DisplayName("액세스_토큰_생성_테스트")
    void createAccessTokenTest() {
        //given
        //when
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);

        //then
        assertThat(accessToken).isNotBlank();
    }

    @Test
    @DisplayName("리프레시_토큰_생성_테스트")
    void createRefreshTokenTest() {
        //given
        //when
        String refreshToken = jwtFactory.createRefreshToken(MEMBER_INFO_KEY);

        //then
        assertThat(refreshToken).isNotBlank();
    }

    @Test
    @DisplayName("액세스_토큰으로_페이로드_추출_테스트")
    void getPayloadTestByAccessToken() {
        //given
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);

        //when
        String payload = jwtFactory.getClaimValue(
            accessToken,
            JwtConstants.MEMBER_ID.getValue()
        );

        //then
        assertThat(payload).isEqualTo(String.valueOf(MEMBER_ID));
    }

    @Test
    @DisplayName("리프레시_토큰으로_페이로드_추출_테스트")
    void getPayloadTestByRefreshToken() {
        //given
        String refreshToken = jwtFactory.createRefreshToken(MEMBER_INFO_KEY);

        //when
        String payload = jwtFactory.getClaimValue(
            refreshToken,
            JwtConstants.MEMBER_INFO_KEY.getValue()
        );

        //the
        assertThat(payload).isEqualTo(MEMBER_INFO_KEY);
    }

    @Test
    @DisplayName("유효하지_않은_토큰으로_페이로드_추출_테스트")
    void getPayloadTestByUnValidClaimName() {
        //given
        //when
        String accessToken = notValidJwtFactory.createAccessToken(MEMBER_ID);

        //then
        assertThatThrownBy(
            () -> jwtFactory.getClaimValue(
                accessToken,
                JwtConstants.MEMBER_ID.getValue()
            )
        ).isExactlyInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("유효한_토큰으로_유효성_검증_테스트")
    void isValidTestByValidToken() {
        //given
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);

        //when
        boolean isValid = jwtFactory.isValid(accessToken);

        //then
        assertThat(isValid).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Bearer ", "trash"})
    @DisplayName("쓰레기 값으로_유효성_검증_테스트")
    void isValidTestByTrashString() {
        //given
        //when
        boolean isValid = jwtFactory.isValid(TRASH);

        //then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("위변조된_토큰으로_유효성_검증_테스트")
    void isValidTestByInvalidToken() {
        //given
        String accessToken = notValidJwtFactory.createAccessToken(MEMBER_ID);

        //when
        boolean isValid = jwtFactory.isValid(accessToken);

        //then
        assertThat(isValid).isFalse();
    }
}