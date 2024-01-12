package site.timecapsulearchive.core.domain.auth.dto.oauth;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class CustomOAuth2User extends DefaultOAuth2User {

    protected final boolean isVerified;
    private final String email;

    public CustomOAuth2User(
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey, String email, boolean isVerified) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.isVerified = isVerified;
    }

    public boolean isNotVerified() {
        return !isVerified;
    }
}
