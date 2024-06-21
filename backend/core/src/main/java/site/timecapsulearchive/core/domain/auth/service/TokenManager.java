package site.timecapsulearchive.core.domain.auth.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.data.dto.TemporaryTokenDto;
import site.timecapsulearchive.core.domain.auth.data.dto.TokenDto;
import site.timecapsulearchive.core.domain.auth.exception.AlreadyReIssuedTokenException;
import site.timecapsulearchive.core.domain.auth.repository.RefreshTokenCacheRepository;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;
import site.timecapsulearchive.core.global.security.jwt.JwtFactory;
import site.timecapsulearchive.core.global.security.jwt.TokenParseResult;
import site.timecapsulearchive.core.global.security.jwt.TokenType;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtFactory jwtFactory;
    private final RefreshTokenCacheRepository refreshTokenCacheRepository;

    /**
     * 새로운 액세스 토큰과 리프레시 토큰을 발급한다. - 액세스 토큰(멤버 아이디) - 리프레시 토큰(데이터베이스에 저장된 사용자 식별자)
     *
     * @param memberId 액세스 토큰 클레임 값에 넣을 사용자 아이디
     * @return 토큰 응답(액세스 토큰, 리프레시 토큰, 액세스 토큰 만료일, 리프레시 토큰 만료일)
     */
    public TokenDto createNewToken(final Long memberId) {
        /**
         * 1. 회원 아이디로 리프레시 토큰 생성
         * 2. 생성된 리프레시 토큰과 회원 아이디로 "1:ejfkdsvxlcvmlxc"로 redis에 저장한다.
         * 3. 생성된 토큰을 반환한다.
         *
         * 1. 회원 아이디로 리프레시 토큰을 찾는다
         * 2. 찾은 리프레시 토큰과 요청 받은 리프레시 토큰을 비교한다.
         * 3. 일치하면 새로운 리프레시 토큰과 액세스 토큰을 반환한다
         * 4. 일치하지 않으면 예외를 반환한다.
         */
        String refreshToken = jwtFactory.createRefreshToken(memberId);
        refreshTokenCacheRepository.save(memberId, refreshToken);

        return createToken(memberId, refreshToken);
    }

    private TokenDto createToken(final Long memberId, final String refreshToken) {
        return TokenDto.create(
            jwtFactory.createAccessToken(memberId),
            refreshToken,
            jwtFactory.getExpiresIn(),
            jwtFactory.getRefreshTokenExpiresIn()
        );
    }

    /**
     * 임시 인증 토큰(1시간)을 발급한다.
     *
     * @param memberId 임시 인증 토큰 클레임 값에 사용할 사용자 아이디
     * @return 임시 인증 토큰 응답(임시 인증 토큰, 임시 인증 토큰 만료일)
     */
    public TemporaryTokenDto createTemporaryToken(final Long memberId) {
        return TemporaryTokenDto.create(
            jwtFactory.createTemporaryAccessToken(memberId),
            jwtFactory.getTemporaryTokenExpiresIn()
        );
    }

    /**
     * 리프레시 토큰으로 새로운 액세스 토큰을 발급한다. 재발급 받은 리프레시 토큰은 다시 사용할 수 없다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 토큰 응답(액세스 토큰, 리프레시 토큰, 액세스 토큰 만료일, 리프레시 토큰 만료일)
     */
    public TokenDto reIssueToken(final String refreshToken) {
        final TokenParseResult tokenParseResult = jwtFactory.parse(
            refreshToken,
            List.of(TokenType.REFRESH)
        );

        Long memberId = Long.valueOf(tokenParseResult.subject());
        final String foundRefreshToken = refreshTokenCacheRepository.findRefreshTokenByMemberId(
                memberId)
            .orElseThrow(AlreadyReIssuedTokenException::new);

        if (!refreshToken.equals(foundRefreshToken)) {
            throw new InvalidTokenException();
        }

        String newRefreshToken = jwtFactory.createRefreshToken(memberId);
        refreshTokenCacheRepository.save(memberId, newRefreshToken);

        return createToken(memberId, refreshToken);
    }
}
