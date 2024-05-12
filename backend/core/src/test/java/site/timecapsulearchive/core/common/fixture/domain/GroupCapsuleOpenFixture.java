package site.timecapsulearchive.core.common.fixture.domain;

import java.util.List;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class GroupCapsuleOpenFixture {

    public static List<GroupCapsuleOpen> groupCapsuleOpens(Boolean isOpened, Capsule capsule,
        List<Member> groupMembers) {
        return groupMembers.stream()
            .map(member -> GroupCapsuleOpen.builder()
                .isOpened(isOpened)
                .capsule(capsule)
                .member(member)
                .build()
            ).toList();
    }

    public static GroupCapsuleOpen groupCapsuleOpen(Member member, Capsule capsule,
        Boolean isOpened) {
        return GroupCapsuleOpen.createOf(member, capsule, isOpened);
    }
}
