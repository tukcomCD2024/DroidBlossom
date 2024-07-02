package site.timecapsulearchive.core.global.security.jwt;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.auth.repository.BlackListCacheRepository;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);
    private final JwtFactory jwtFactory;
    private final BlackListCacheRepository blackListCacheRepository;

    @Override
    public Authentication authenticate(final Authentication authentication)
        throws AuthenticationException {
        final String accessToken = (String) authentication.getCredentials();

        jwtFactory.validate(accessToken);

        final TokenParseResult tokenParseResult = jwtFactory.parse(
            accessToken, List.of(TokenType.ACCESS, TokenType.TEMPORARY)
        );

        final Long memberId = getMemberId(tokenParseResult);

        final String blackListToken = blackListCacheRepository.findBlackListTokenByMemberId(
            memberId);
        if (accessToken.equals(blackListToken)) {
            log.error("블랙리스트 토큰으로 접근 시도 id: {}, blackListToken: {}, accessToken: {}", memberId,
                blackListToken, accessToken);
            throw new InvalidTokenException();
        }

        if (tokenParseResult.tokenType() == TokenType.TEMPORARY) {
            return JwtAuthenticationToken.authenticatedWithTemporary(memberId);
        }

        return JwtAuthenticationToken.authenticatedWithAccess(memberId, accessToken);
    }

    private Long getMemberId(final TokenParseResult tokenParseResult) {
        return Long.valueOf(tokenParseResult.subject());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
