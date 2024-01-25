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

    private static final String MEMBER_ID_CLAIM = JwtConstants.MEMBER_ID.getValue();
    private static final String MEMBER_INFO_CLAIM = JwtConstants.MEMBER_INFO_KEY.getValue();

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
            .claim(MEMBER_ID_CLAIM, memberId.toString())
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 사용자 식별자를 받아서 리프레시 토큰 반환
     *
     * @param memberProfileKey 사용자 식별자
     * @return 리프레시 토큰
     */
    public String createRefreshToken(final String memberProfileKey) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityMs);

        return Jwts.builder()
            .claim(MEMBER_INFO_CLAIM, memberProfileKey)
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
            .claim(MEMBER_ID_CLAIM, memberId.toString())
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰과 클레임 키로 클레임 값 파싱
     *
     * @param token    토큰
     * @param claimKey 파싱할 클레임 키
     * @return 클레임 키에 따른 값
     */
    public String getClaimValue(final String token, final String claimKey) {
        try {
            return jwtParser()
                .parseClaimsJws(token)
                .getBody()
                .get(claimKey, String.class);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(e);
        }
    }

    private JwtParser jwtParser() {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    /**
     * 토큰을 파싱해서 올바른 토큰인지 확인
     *
     * @param token 검증할 토큰
     * @return 유효한 토큰이면 {@code true}
     */
    public boolean isValid(final String token) {
        try {
            jwtParser().parseClaimsJws(token);

            return true;
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getExpiresIn() {
        return String.valueOf(accessTokenValidityMs);
    }

    public String getRefreshTokenExpiresIn() {
        return String.valueOf(refreshTokenValidityMs);
    }

    public String getTemporaryTokenExpiresIn() {
        return String.valueOf(temporaryValidityMs);
    }
}
