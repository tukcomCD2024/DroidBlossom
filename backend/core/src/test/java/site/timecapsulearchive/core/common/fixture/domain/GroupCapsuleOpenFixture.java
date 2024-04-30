package site.timecapsulearchive.core.common.fixture.domain;

import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class GroupCapsuleOpenFixture {

    public static GroupCapsuleOpen groupCapsuleOpen(Boolean isOpened, Capsule capsule, Member member) {
        return GroupCapsuleOpen.builder()
            .isOpened(isOpened)
            .capsule(capsule)
            .member(member)
            .build();
    }

}
