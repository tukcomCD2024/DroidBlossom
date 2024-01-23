package site.timecapsulearchive.core.global.security.oauth.dto.google;

import java.util.Map;
import site.timecapsulearchive.core.global.security.oauth.dto.OAuth2UserInfo;

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
