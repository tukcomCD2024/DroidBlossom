package site.timecapsulearchive.core.global.security.jwt;

public record TokenParseResult(
    String subject,
    TokenType tokenType
) {

    public static TokenParseResult of(final String subject, final TokenType tokenType) {
        return new TokenParseResult(subject, tokenType);
    }
}
