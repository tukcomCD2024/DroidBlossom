package site.timecapsulearchive.core.global.security.jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class JwtAuthenticationToken implements Authentication {

    private final List<GrantedAuthority> authorities;
    private final String accessToken;
    private final Long memberId;
    private boolean authenticated;

    private JwtAuthenticationToken(
        String accessToken,
        Long memberId,
        boolean authenticated
    ) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.authorities = Collections.emptyList();
        this.authenticated = authenticated;
    }

    public static JwtAuthenticationToken authenticated(Long memberId) {
        return new JwtAuthenticationToken(null, memberId, true);
    }

    public static JwtAuthenticationToken unauthenticated(String accessToken) {
        return new JwtAuthenticationToken(accessToken, null, false);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getDetails() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return memberId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "false로만 설정이 가능합니다. 생성자를 이용하세요");
        this.authenticated = false;
    }

    @Override
    public String getName() {
        return "";
    }
}
