package site.timecapsulearchive.core.domain.auth.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.auth.dto.MemberInfo;
import site.timecapsulearchive.core.domain.auth.dto.response.TemporaryTokenResponse;
import site.timecapsulearchive.core.domain.auth.dto.response.TokenResponse;
import site.timecapsulearchive.core.domain.auth.exception.AlreadyReIssuedTokenException;
import site.timecapsulearchive.core.domain.auth.repository.MemberInfoCacheRepository;
import site.timecapsulearchive.core.global.security.jwt.JwtFactory;
import site.timecapsulearchive.core.global.security.jwt.TokenParseResult;
import site.timecapsulearchive.core.global.security.jwt.TokenType;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final JwtFactory jwtFactory;
    private final MemberInfoCacheRepository memberInfoCacheRepository;

    /**
     * 새로운 액세스 토큰과 리프레시 토큰을 발급한다. - 액세스 토큰(멤버 아이디) - 리프레시 토큰(데이터베이스에 저장된 사용자 식별자)
     *
     * @param memberId 액세스 토큰 클레임 값에 넣을 사용자 아이디
     * @return 토큰 응답(액세스 토큰, 리프레시 토큰, 액세스 토큰 만료일, 리프레시 토큰 만료일)
     */
    public TokenResponse createNewToken(final Long memberId) {
        String key = String.valueOf(UUID.randomUUID());
        memberInfoCacheRepository.save(key, MemberInfo.from(memberId));

        return createTokenResponse(memberId, key);
    }

    private TokenResponse createTokenResponse(final Long memberId, final String key) {
        return TokenResponse.create(
            jwtFactory.createAccessToken(memberId),
            jwtFactory.createRefreshToken(key),
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
    public TemporaryTokenResponse createTemporaryToken(final Long memberId) {
        return TemporaryTokenResponse.create(
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
    public TokenResponse reIssueToken(final String refreshToken) {
        TokenParseResult tokenParseResult = jwtFactory.parse(
            refreshToken,
            List.of(TokenType.REFRESH)
        );
        MemberInfo memberInfo = memberInfoCacheRepository.getMemberInfo(tokenParseResult.subject())
            .orElseThrow(AlreadyReIssuedTokenException::new);

        String newKey = String.valueOf(UUID.randomUUID());
        memberInfoCacheRepository.rename(tokenParseResult.subject(), newKey);

        return createTokenResponse(memberInfo.memberId(), newKey);
    }
}
