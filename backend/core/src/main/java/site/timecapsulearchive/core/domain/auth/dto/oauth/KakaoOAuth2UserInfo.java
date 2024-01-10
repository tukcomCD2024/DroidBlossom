package site.timecapsulearchive.core.domain.auth.dto.oauth;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private static final String PROFILE_IMAGE_URL = "profile_image_url";
    private static final String KAKAO_ACCOUNT = "kakao_account";
    private static final String PROFILE = "profile";
    private static final String EMAIL = "email";

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    private static Map<String, Object> getKakaoPofile(Map<String, Object> account) {
        Map<String, Object> profile = (Map<String, Object>) account.get(PROFILE);
        return profile;
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = getKakaoAcoount();
        if (account == null) {
            return null;
        }

        return (String) account.get(EMAIL);
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = getKakaoAcoount();
        if (account == null) {
            return null;
        }

        Map<String, Object> profile = getKakaoPofile(account);
        if (profile == null) {
            return null;
        }

        return (String) profile.get(PROFILE_IMAGE_URL);
    }

    private Map<String, Object> getKakaoAcoount() {
        Map<String, Object> account = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT);
        return account;
    }


}
