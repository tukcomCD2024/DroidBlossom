package site.timecapsulearchive.core.global.security.jwt;

public record TokenParseResult(
    String subject,
    TokenType tokenType
) {

    public static TokenParseResult of(String subject, TokenType tokenType) {
        return new TokenParseResult(subject, tokenType);
    }
}
