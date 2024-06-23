package site.timecapsulearchive.core.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.auth.data.dto.TemporaryTokenDto;
import site.timecapsulearchive.core.domain.auth.data.dto.TokenDto;
import site.timecapsulearchive.core.domain.auth.exception.AlreadyReIssuedTokenException;
import site.timecapsulearchive.core.domain.auth.repository.RefreshTokenCacheRepository;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;
import site.timecapsulearchive.core.global.security.jwt.JwtFactory;

class TokenManagerTest {

    private static final Long MEMBER_ID = 1L;

    private final JwtFactory jwtFactory = UnitTestDependency.jwtFactory();
    private final RefreshTokenCacheRepository refreshTokenCacheRepository = mock(
        RefreshTokenCacheRepository.class);
    private final TokenManager tokenManager = new TokenManager(jwtFactory, refreshTokenCacheRepository);

    @Test
    void 사용자_아이디로_토큰을_발급하면_액세스_토큰과_리프레시_토큰이_나온다() {
        //given
        //when
        TokenDto newToken = tokenManager.createNewToken(MEMBER_ID);

        //then
        assertSoftly(softly -> {
            softly.assertThat(newToken.accessToken()).isNotBlank();
            softly.assertThat(newToken.refreshToken()).isNotBlank();
            softly.assertThat(newToken.expiresIn()).isGreaterThan(0);
            softly.assertThat(newToken.refreshTokenExpiresIn()).isGreaterThan(0);
        });
    }

    @Test
    void 사용자_아이디로_임시인증_토큰을_발급하면_임시_액세스_토큰이_나온다() {
        //given
        //when
        TemporaryTokenDto newToken = tokenManager.createTemporaryToken(MEMBER_ID);

        //then
        assertSoftly(softly -> {
            softly.assertThat(newToken.temporaryAccessToken()).isNotBlank();
            softly.assertThat(newToken.expiresIn()).isGreaterThan(0);
        });
    }

    @Test
    void 리프레시_토큰으로_재발급하면_액세스_토큰과_리프레시_토큰이_나온다() {
        //given
        String refreshToken = jwtFactory.createRefreshToken(MEMBER_ID);
        given(refreshTokenCacheRepository.findRefreshTokenByMemberId(anyLong()))
            .willReturn(Optional.of(refreshToken));

        //when
        TokenDto refreshedToken = tokenManager.reIssueToken(refreshToken);

        //then
         assertSoftly(softly -> {
            softly.assertThat(refreshedToken.accessToken()).isNotBlank();
            softly.assertThat(refreshedToken.refreshToken()).isNotBlank();
            softly.assertThat(refreshedToken.expiresIn()).isGreaterThan(0);
            softly.assertThat(refreshedToken.refreshTokenExpiresIn()).isGreaterThan(0);
        });
    }

    @Test
    void 저장되지_않은_리프레시_토큰으로_재발급하면_예외가_발생한다() {
        //given
        String refreshToken = jwtFactory.createRefreshToken(MEMBER_ID);
        given(refreshTokenCacheRepository.findRefreshTokenByMemberId(anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> tokenManager.reIssueToken(refreshToken))
            .isInstanceOf(AlreadyReIssuedTokenException.class);
    }

    @Test
    void 재발급에_사용할_리프레시_토큰과_저장된_리프레시_토큰이_일치하지_않으면_예외가_발생한다() {
        //given
        String refreshToken = jwtFactory.createRefreshToken(MEMBER_ID);
        given(refreshTokenCacheRepository.findRefreshTokenByMemberId(anyLong()))
            .willReturn(Optional.of(jwtFactory.createRefreshToken(2L)));

        //when
        //then
        assertThatThrownBy(() -> tokenManager.reIssueToken(refreshToken))
            .isInstanceOf(InvalidTokenException.class);
    }
}