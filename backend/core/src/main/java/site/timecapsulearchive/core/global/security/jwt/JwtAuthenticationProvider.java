package site.timecapsulearchive.core.global.security.jwt;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtFactory jwtFactory;

    @Override
    public Authentication authenticate(final Authentication authentication)
        throws AuthenticationException {
        final String accessToken = (String) authentication.getCredentials();

        jwtFactory.validate(accessToken);

        final TokenParseResult tokenParseResult = jwtFactory.parse(
            accessToken, List.of(TokenType.ACCESS, TokenType.TEMPORARY)
        );

        final Long memberId = getMemberId(tokenParseResult);
        if (tokenParseResult.tokenType() == TokenType.TEMPORARY) {
            return JwtAuthenticationToken.authenticatedWithTemporary(memberId);
        }

        return JwtAuthenticationToken.authenticatedWithAccess(memberId);
    }

    private Long getMemberId(final TokenParseResult tokenParseResult) {
        return Long.valueOf(tokenParseResult.subject());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
