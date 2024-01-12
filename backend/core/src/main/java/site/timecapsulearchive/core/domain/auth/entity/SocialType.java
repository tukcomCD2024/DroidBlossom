package site.timecapsulearchive.core.domain.auth.entity;

public enum SocialType {
    KAKAO, GOOGLE;

    public static SocialType getSocialType(String registrationId) {
        if (isKakaoLogin(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    private static boolean isKakaoLogin(String registrationId) {
        return registrationId.equals("kakao");
    }
}
