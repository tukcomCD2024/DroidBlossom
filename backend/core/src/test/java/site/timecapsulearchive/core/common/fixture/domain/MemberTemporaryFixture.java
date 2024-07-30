package site.timecapsulearchive.core.common.fixture.domain;

import site.timecapsulearchive.core.domain.member.entity.MemberTemporary;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

public class MemberTemporaryFixture {

    public static MemberTemporary memberTemporary(Long memberId) {
        return MemberTemporary.builder()
            .authId(memberId + "test_auth_id")
            .tag(memberId + "test_tag")
            .email(memberId + "test_email@gmail.com")
            .nickname(memberId + "test_nickname")
            .profileUrl(memberId + "test_profile_url")
            .socialType(SocialType.GOOGLE)
            .build();
    }
}
