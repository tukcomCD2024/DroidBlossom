package site.timecapsulearchive.core.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.config.security.JwtProperties;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@Component
public class JwtFactory {

    private static final String ISSUER = "https://archive-timecapsule.kro.kr";
    private static final String TOKEN_TYPE_CLAIM_NAME = "token_type";

    private final SecretKey key;
    private final long accessTokenValidityMs;
    private final long refreshTokenValidityMs;
    private final long temporaryValidityMs;

    public JwtFactory(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityMs = jwtProperties.accessTokenValidityMs();
        this.refreshTokenValidityMs = jwtProperties.refreshTokenValidityMs();
        this.temporaryValidityMs = jwtProperties.temporaryTokenValidityMs();
    }

    /**
     * 사용자 아이디를 받아서 엑세스 토큰 반환
     *
     * @param memberId 사용자 아이디
     * @return 액세스 토큰
     */
    public String createAccessToken(final Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityMs);

        return Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(String.valueOf(memberId))
            .setExpiration(validity)
            .claim(TOKEN_TYPE_CLAIM_NAME, TokenType.ACCESS.name())
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 사용자 식별자를 받아서 리프레시 토큰 반환
     *
     * @param memberId 사용자 식별자
     * @return 리프레시 토큰
     */
    public String createRefreshToken(final Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityMs);

        return Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(String.valueOf(memberId))
            .setExpiration(validity)
            .claim(TOKEN_TYPE_CLAIM_NAME, TokenType.REFRESH.name())
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 사용자 아이디를 받아서 임시 토큰 (１시간) 토큰 반환
     *
     * @param memberId 사용자 아이디
     * @return 임시 인증 토큰
     */
    public String createTemporaryAccessToken(final Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + temporaryValidityMs);

        return Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(String.valueOf(memberId))
            .setExpiration(validity)
            .claim(TOKEN_TYPE_CLAIM_NAME, TokenType.TEMPORARY)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰과 토큰 타입으로 토큰의 사용자 식별자와 타입 추출
     *
     * @param token 토큰
     * @return 사용자 식별자, 토큰 타입
     */
    public TokenParseResult parse(final String token, final List<TokenType> acceptableTokenTypes) {
        try {
            Claims claims = jwtParser()
                .parseClaimsJws(token)
                .getBody();

            TokenType tokenType = getAcceptableTokenType(acceptableTokenTypes, claims);

            return TokenParseResult.of(
                claims.getSubject(),
                tokenType
            );
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(e);
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    private TokenType getAcceptableTokenType(List<TokenType> tokenTypes, Claims claims) {
        String extractTokenType = claims.get(TOKEN_TYPE_CLAIM_NAME, String.class);
        if (extractTokenType == null || extractTokenType.isBlank()) {
            throw new IllegalArgumentException();
        }

        TokenType tokenType = TokenType.valueOf(extractTokenType);
        if (!tokenTypes.contains(tokenType)) {
            throw new JwtException("허용되지 않는 JWT 토큰 타입입니다.");
        }
        return tokenType;
    }

    /**
     * 토큰을 파싱해서 올바른 토큰인지 확인
     *
     * @param token 검증할 토큰
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    public void validate(final String token) {
        try {
            jwtParser().parseClaimsJws(token);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(e);
        }
    }

    public long getExpiresIn() {
        return accessTokenValidityMs;
    }

    public long getRefreshTokenExpiresIn() {
        return refreshTokenValidityMs;
    }

    public long getTemporaryTokenExpiresIn() {
        return temporaryValidityMs;
    }

    public long getLeftTime(final String accessToken) {
        Instant expiration;
        try {
            expiration = jwtParser().parseClaimsJws(accessToken)
                .getBody()
                .getExpiration()
                .toInstant();
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(e);
        }

        Instant now = Instant.now();

        return Duration.between(now, expiration).toMillis();
    }
}
