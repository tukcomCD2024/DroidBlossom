package site.timecapsulearchive.core.global.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.common.fixture.security.TokenFixture;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

class JwtFactoryTest {

    private static final Long MEMBER_ID = 1L;

    private JwtFactory jwtFactory = UnitTestDependency.jwtFactory();

    @Test
    void 사용자_아이디로_액세스_토큰을_생성하면_액세스_토큰이_나온다() {
        //given
        //when
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);

        //then
        assertThat(accessToken).isNotBlank();
    }

    @Test
    void 사용자_아이디로_리프레시_토큰을_생성하면_리프레시_토큰이_나온다() {
        //given
        //when
        String refreshToken = jwtFactory.createRefreshToken(MEMBER_ID);

        //then
        assertThat(refreshToken).isNotBlank();
    }

    @Test
    void 사용자_아이디로_임시_인증_토큰을_생성하면_임시_인증_토큰이_나온다() {
        //given
        //when
        String temporaryAccessToken = jwtFactory.createTemporaryAccessToken(MEMBER_ID);

        //then
        assertThat(temporaryAccessToken).isNotBlank();
    }

    @Test
    void 사용자_아이디로_액세스_토큰을_생성후_파싱하면_사용자_아이디가_나온다() {
        //given
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);

        //when
        TokenParseResult result = jwtFactory.parse(accessToken, List.of(TokenType.ACCESS));

        //then
        assertThat(Long.valueOf(result.subject())).isEqualTo(MEMBER_ID);
    }

    @Test
    void 사용자_아이디로_액세스_토큰을_생성후_파싱하면_액세스_토큰_타입이_나온다() {
        //given
        String accessToken = jwtFactory.createAccessToken(MEMBER_ID);

        //when
        TokenParseResult result = jwtFactory.parse(accessToken, List.of(TokenType.ACCESS));

        //then
        assertThat(result.tokenType()).isEqualTo(TokenType.ACCESS);
    }

    @Test
    void 사용자_아이디로_리프레시_토큰을_생성후_파싱하면_사용자_아이디가_나온다() {
        //given
        String accessToken = jwtFactory.createRefreshToken(MEMBER_ID);

        //when
        TokenParseResult result = jwtFactory.parse(accessToken, List.of(TokenType.REFRESH));

        //then
        assertThat(Long.valueOf(result.subject())).isEqualTo(MEMBER_ID);
    }

    @Test
    void 사용자_아이디로_리프레시_토큰을_생성후_파싱하면_액세스_토큰_타입이_나온다() {
        //given
        String accessToken = jwtFactory.createRefreshToken(MEMBER_ID);

        //when
        TokenParseResult result = jwtFactory.parse(accessToken, List.of(TokenType.REFRESH));

        //then
        assertThat(result.tokenType()).isEqualTo(TokenType.REFRESH);
    }

    @Test
    void 사용자_아이디로_임시인증_토큰을_생성후_파싱하면_사용자_아이디가_나온다() {
        //given
        String accessToken = jwtFactory.createTemporaryAccessToken(MEMBER_ID);

        //when
        TokenParseResult result = jwtFactory.parse(accessToken, List.of(TokenType.TEMPORARY));

        //then
        assertThat(Long.valueOf(result.subject())).isEqualTo(MEMBER_ID);
    }

    @Test
    void 사용자_아이디로_임시인증_토큰을_생성후_파싱하면_액세스_토큰_타입이_나온다() {
        //given
        String accessToken = jwtFactory.createTemporaryAccessToken(MEMBER_ID);

        //when
        TokenParseResult result = jwtFactory.parse(accessToken, List.of(TokenType.TEMPORARY));

        //then
        assertThat(result.tokenType()).isEqualTo(TokenType.TEMPORARY);
    }

    @Test
    void 토큰_타입이_공백인_토큰을_파싱하면_예외가_발생한다() {
        //given
        String token = TokenFixture.invalidTokenType(MEMBER_ID, "");
        List<TokenType> tokenTypes = List.of(TokenType.ACCESS, TokenType.REFRESH);

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.parse(token, tokenTypes))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 토큰_타입이_없는_토큰을_파싱하면_예외가_발생한다() {
        //given
        String token = TokenFixture.invalidTokenType(MEMBER_ID, null);
        List<TokenType> tokenTypes = List.of(TokenType.ACCESS, TokenType.REFRESH);

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.parse(token, tokenTypes))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 추출할_토큰_타입이_다른_토큰을_파싱하면_예외가_발생한다() {
        //given
        String token = jwtFactory.createAccessToken(MEMBER_ID);
        List<TokenType> tokenTypes = List.of(TokenType.REFRESH);

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.parse(token, tokenTypes))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 서명이_다른_jwt_생성기로_생성한_토큰을_검증하면_예외가_발생한다() {
        //given
        String accessToken = TokenFixture.forgedSignature(MEMBER_ID, TokenType.ACCESS);

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.validate(accessToken))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 시간이_지난_토큰으로_검증하면_예외가_발생한다() {
        //given
        String expiredToken = TokenFixture.expiredToken(MEMBER_ID, TokenType.ACCESS);

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.validate(expiredToken))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 유효하지_않은_형식의_토큰으로_검증하면_예외가_발생한다() {
        //given
        String invalidToken = TokenFixture.invalidToken();

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.validate(invalidToken))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 액세스_토큰을_주면_현재_시간과의_차이_밀리초를_반환한다() {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);

        //when
        long leftTime = jwtFactory.getLeftTime(accessToken);

        //then
        assertThat(leftTime).isPositive();
    }

    @Test
    void 이미_지난_액세스_토큰을_주면_예외가_발생한다() {
        //given
        String expiredAccessToken = TokenFixture.expiredToken(MEMBER_ID, TokenType.ACCESS);

        //when
        //then
        assertThatThrownBy(() -> jwtFactory.getLeftTime(expiredAccessToken))
            .isInstanceOf(InvalidTokenException.class);
    }
}