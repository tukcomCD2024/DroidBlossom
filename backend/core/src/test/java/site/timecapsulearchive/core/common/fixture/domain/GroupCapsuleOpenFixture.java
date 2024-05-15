package site.timecapsulearchive.core.common.fixture.domain;

import java.util.List;
import java.util.Optional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
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

    public static Optional<GroupCapsuleOpen> groupCapsuleOpen(int dataPrefix) {
        Member member = MemberFixture.member(dataPrefix);
        CapsuleSkin capsuleSkin = CapsuleSkinFixture.capsuleSkin(member);

        return Optional.of(
            GroupCapsuleOpen.createOf(
                MemberFixture.member(dataPrefix),
                CapsuleFixture.capsule(member, capsuleSkin, CapsuleType.GROUP),
                Boolean.FALSE
            )
        );
    }
}
