package site.timecapsulearchive.core.global.security.oauth.dto.kakao;

import java.util.Map;
import site.timecapsulearchive.core.global.security.oauth.dto.OAuth2UserInfo;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private static final String PROFILE_IMAGE_URL = "profile_image_url";
    private static final String KAKAO_ACCOUNT = "kakao_account";
    private static final String PROFILE = "profile";
    private static final String EMAIL = "email";

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    private static Map<String, Object> getKakaoPofile(final Map<String, Object> account) {
        return (Map<String, Object>) account.get(PROFILE);
    }

    @Override
    public String getEmail() {
        final Map<String, Object> account = getKakaoAcoount();
        if (account == null) {
            return null;
        }

        return (String) account.get(EMAIL);
    }

    @Override
    public String getImageUrl() {
        final Map<String, Object> account = getKakaoAcoount();
        if (account == null) {
            return null;
        }

        final Map<String, Object> profile = getKakaoPofile(account);
        if (profile == null) {
            return null;
        }

        return (String) profile.get(PROFILE_IMAGE_URL);
    }

    private Map<String, Object> getKakaoAcoount() {
        return (Map<String, Object>) attributes.get(KAKAO_ACCOUNT);
    }


}
