package site.timecapsulearchive.core.global.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.config.security.JwtProperties;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@Component
public class JwtFactory {

    private static final String ISSUER = "https://archive-timecapsule.kro.kr";
    private static final String TOKEN_TYPE_CLAIM_NAME = "type";

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
            .claim(TOKEN_TYPE_CLAIM_NAME, TokenType.ACCESS.getValue())
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 사용자 식별자를 받아서 리프레시 토큰 반환
     *
     * @param memberInfoKey 사용자 식별자
     * @return 리프레시 토큰
     */
    public String createRefreshToken(final String memberInfoKey) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityMs);

        return Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(memberInfoKey)
            .claim(TOKEN_TYPE_CLAIM_NAME, TokenType.REFRESH.getValue())
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 사용자 아이디를 받아서 임시 토큰 (１시간) 토큰 반환
     *
     * @param memberId 사용자 아이디
     * @return 리프레시 토큰
     */
    public String createTemporaryAccessToken(final Long memberId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + temporaryValidityMs);

        return Jwts.builder()
            .setIssuer(ISSUER)
            .setSubject(String.valueOf(memberId))
            .claim(TOKEN_TYPE_CLAIM_NAME, TokenType.TEMPORARY.getValue())
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰과 토큰 타입으로 토큰의 사용자 식별자 추출
     *
     * @param token 토큰
     * @param type  토큰의 타입
     * @return 사용자 식별자
     */
    public String getSubject(final String token, TokenType type) {
        try {
            return jwtParser(type)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(e);
        }
    }

    private JwtParser jwtParser(TokenType type) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .require(TOKEN_TYPE_CLAIM_NAME, type.getValue())
            .build();
    }

    /**
     * 토큰을 파싱해서 올바른 토큰인지 확인
     *
     * @param token 검증할 토큰
     */
    public void validate(final String token, final TokenType type) {
        try {
            jwtParser(type).parseClaimsJws(token);
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
}
