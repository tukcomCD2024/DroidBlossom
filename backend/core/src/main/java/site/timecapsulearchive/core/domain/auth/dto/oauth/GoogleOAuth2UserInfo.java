package site.timecapsulearchive.core.domain.auth.dto.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    private static final String EMAIL = "email";
    private static final String PICTURE = "picture";

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get(EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get(PICTURE);
    }
}
