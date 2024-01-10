package site.timecapsulearchive.core.domain.auth.dto.oauth;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import site.timecapsulearchive.core.domain.auth.entity.Role;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final String email;
    private final Role role;

    public CustomOAuth2User(
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey, String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}
