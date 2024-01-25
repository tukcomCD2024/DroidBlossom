package site.timecapsulearchive.core.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.error.exception.InvalidTokenException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final String MEMBER_ID_CLAIM_KEY = JwtConstants.MEMBER_ID.getValue();

    private final JwtFactory jwtFactory;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();

        if (isNotValid(accessToken)) {
            throw new InvalidTokenException();
        }

        String memberId = jwtFactory.getClaimValue(
            accessToken,
            MEMBER_ID_CLAIM_KEY
        );

        return JwtAuthenticationToken.authenticated(Long.valueOf(memberId));
    }

    private boolean isNotValid(String accessToken) {
        return !jwtFactory.isValid(accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
