package site.timecapsulearchive.core.global.security.jwt;

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
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        String accessToken = (String) authentication.getCredentials();

        jwtFactory.validate(accessToken);

        String memberId = jwtFactory.getSubject(
            accessToken
        );

        return JwtAuthenticationToken.authenticated(Long.valueOf(memberId));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
