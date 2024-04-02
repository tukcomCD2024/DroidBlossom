package site.timecapsulearchive.core.common.data.fixture;

import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class CapsuleSkinFixture {

    public static CapsuleSkin testCapsuleSkin(int dataPrefix, Member member) {
        return CapsuleSkin.builder()
            .skinName(dataPrefix + "testSkinName")
            .imageUrl(dataPrefix + "testImageUrl")
            .motionName(null)
            .retarget(null)
            .member(member)
            .build();
    }

}
