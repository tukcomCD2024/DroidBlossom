package site.timecapsulearchive.core.common.fixture.domain;

import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Motion;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Retarget;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class CapsuleSkinFixture {

    public static CapsuleSkin capsuleSkin(Member member) {
        return CapsuleSkin.builder()
            .skinName("test")
            .imageUrl("test-url")
            .motionName(Motion.DAB)
            .retarget(Retarget.FAIR)
            .member(member)
            .build();
    }
}
