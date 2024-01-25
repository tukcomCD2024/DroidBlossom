package site.timecapsulearchive.core.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SocialType {
    KAKAO, GOOGLE;

    @JsonCreator
    public static SocialType from(String s) {
        return SocialType.valueOf(s.toUpperCase());
    }

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
