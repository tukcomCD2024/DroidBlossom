package site.timecapsulearchive.core.common.fixture.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.global.config.security.JwtProperties;
import site.timecapsulearchive.core.global.security.jwt.JwtFactory;
import site.timecapsulearchive.core.global.security.jwt.TokenType;

public class TokenFixture {

    private static final String TOKEN_TYPE_CLAIM_NAME = "token_type";
    private static final String ISSUER = "https://archive-timecapsule.kro.kr";

    private static final JwtFactory jwtFactory = UnitTestDependency.jwtFactory();
    private static final JwtProperties jwtProperties = UnitTestDependency.jwtProperties();
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(
        jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));

    public static String accessToken(Long memberId) {
        return jwtFactory.createAccessToken(memberId);
    }

    public static String refreshToken(Long memberId) {
        return jwtFactory.createRefreshToken(memberId);
    }

    public static String temporaryAccessToken(Long memberId) {
        return jwtFactory.createTemporaryAccessToken(memberId);
    }

    public static String invalidToken() {
        return "trash";
    }

    public static String expiredToken(Long memberId, TokenType tokenType) {
        Date now = new Date();
        Date validity = new Date(now.getTime() - jwtProperties.accessTokenValidityMs());

        JwtBuilder jwtBuilder = getDefaultJwtBuilder(memberId, validity, secretKey)
            .claim("token_type", tokenType);

        return jwtBuilder.compact();
    }

    private static JwtBuilder getDefaultJwtBuilder(Long memberId, Date validity,
        SecretKey secretKey) {
        JwtBuilder jwtBuilder = Jwts.builder()
            .setExpiration(validity)
            .setIssuer(ISSUER)
            .signWith(secretKey, SignatureAlgorithm.HS256);

        if (memberId != null) {
            jwtBuilder.setSubject(String.valueOf(memberId));
        }

        return jwtBuilder;
    }


    public static String forgedSignature(Long memberId, TokenType tokenType) {
        Date now = new Date();
        Date validity = new Date(now.getTime() - jwtProperties.accessTokenValidityMs());

        SecretKey forgedKey = Keys.hmacShaKeyFor("F".repeat(32).getBytes(
            StandardCharsets.UTF_8));
        JwtBuilder jwtBuilder = getDefaultJwtBuilder(memberId, validity, forgedKey)
            .claim(TOKEN_TYPE_CLAIM_NAME, tokenType);

        return jwtBuilder.compact();
    }

    public static String invalidTokenType(Long memberId, String tokenType) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.accessTokenValidityMs());

        JwtBuilder jwtBuilder = getDefaultJwtBuilder(memberId, validity, secretKey);

        if (tokenType != null) {
            jwtBuilder.claim(TOKEN_TYPE_CLAIM_NAME, tokenType);
        }

        return jwtBuilder.compact();
    }

    public static String noSubject(TokenType tokenType) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.accessTokenValidityMs());

        JwtBuilder jwtBuilder = getDefaultJwtBuilder(null, validity, secretKey)
            .claim(TOKEN_TYPE_CLAIM_NAME, tokenType);

        return jwtBuilder.compact();
    }
}
