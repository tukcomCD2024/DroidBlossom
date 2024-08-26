package site.timecapsulearchive.core.global.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.common.fixture.security.TokenFixture;
import site.timecapsulearchive.core.domain.auth.repository.BlackListCacheRepository;
import site.timecapsulearchive.core.domain.member.entity.Role;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

class JwtAuthenticationProviderTest {

    private static final Long MEMBER_ID = 1L;

    private final BlackListCacheRepository blackListCacheRepository = mock(
        BlackListCacheRepository.class);
    private final JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(
        UnitTestDependency.jwtFactory(), blackListCacheRepository);

    @Test
    void 액세스_토큰으로_인증하면_인증에_성공한다() {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            accessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(null);

        //when
        Authentication authenticateResult = jwtAuthenticationProvider.authenticate(unauthenticated);

        //then
        assertThat(authenticateResult.isAuthenticated()).isTrue();
    }

    @Test
    void 액세스_토큰으로_인증하면_사용자_아이디를_확인할_수_있다() {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            accessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(null);

        //when
        Authentication authenticateResult = jwtAuthenticationProvider.authenticate(unauthenticated);

        //then
        assertThat((Long) authenticateResult.getPrincipal()).isEqualTo(MEMBER_ID);
    }

    @Test
    void 액세스_토큰으로_인증하면_사용자_권한을_확인할_수_있다() {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            accessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(null);

        //when
        Authentication authenticateResult = jwtAuthenticationProvider.authenticate(unauthenticated);

        //then
        assertThat(authenticateResult.getAuthorities())
            .isNotEmpty()
            .allMatch(authority ->
                authority.equals(new SimpleGrantedAuthority(Role.USER.getValue())));
    }

    @Test
    void 임시인증_토큰으로_인증하면_인증에_성공한다() {
        //given
        String temporaryAccessToken = TokenFixture.temporaryAccessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            temporaryAccessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(null);

        //when
        Authentication authenticateResult = jwtAuthenticationProvider.authenticate(unauthenticated);

        //then
        assertThat(authenticateResult.isAuthenticated()).isTrue();
    }

    @Test
    void 임시인증_토큰으로_인증하면_사용자_아이디를_확인할_수_있다() {
        //given
        String temporaryAccessToken = TokenFixture.temporaryAccessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            temporaryAccessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(null);

        //when
        Authentication authenticateResult = jwtAuthenticationProvider.authenticate(unauthenticated);

        //then
        assertThat((Long) authenticateResult.getPrincipal()).isEqualTo(MEMBER_ID);
    }

    @Test
    void 임시인증_토큰으로_인증하면_사용자_권한을_확인할_수_있다() {
        //given
        String temporaryAccessToken = TokenFixture.temporaryAccessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            temporaryAccessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(null);

        //when
        Authentication authenticateResult = jwtAuthenticationProvider.authenticate(unauthenticated);

        //then
        assertThat(authenticateResult.getAuthorities())
            .isNotEmpty()
            .allMatch(authority ->
                authority.equals(new SimpleGrantedAuthority(Role.TEMPORARY.getValue())));
    }

    @Test
    void 유효하지_않은_토큰으로_인증하면_예외가_발생한다() {
        //given
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            TokenFixture.invalidToken());

        //when
        //then
        assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(unauthenticated))
            .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void 블랙리스트에_있는_토큰으로_접근하면_예외가_발생한다() {
        //given
        String accessToken = TokenFixture.accessToken(MEMBER_ID);
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(
            accessToken);
        given(blackListCacheRepository.findBlackListTokenByMemberId(MEMBER_ID)).willReturn(
            accessToken);

        //when
        //then
        assertThatThrownBy(() -> jwtAuthenticationProvider.authenticate(unauthenticated))
            .isInstanceOf(InvalidTokenException.class);
    }
}