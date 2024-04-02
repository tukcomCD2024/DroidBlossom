package site.timecapsulearchive.core.common.data.fixture;

import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.entity.SocialType;

public class MemberFixture {

    public static Member testMember(int dataPrefix) {
        return Member.builder()
            .socialType(SocialType.GOOGLE)
            .nickname(dataPrefix + "testNickname")
            .email(dataPrefix + "test@google.com")
            .authId(dataPrefix + "test")
            .password(dataPrefix + "password")
            .profileUrl(dataPrefix + "test.com")
            .tag(dataPrefix + "testTag")
            .build();
    }

}
