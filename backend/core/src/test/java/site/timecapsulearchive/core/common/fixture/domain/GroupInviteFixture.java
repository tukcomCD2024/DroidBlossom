package site.timecapsulearchive.core.common.fixture.domain;

import java.util.Optional;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member_group.entity.GroupInvite;

public class GroupInviteFixture {

    public static Optional<GroupInvite> groupInvite(Group group, Member groupOwner,
        Member groupMember) {
        return Optional.of(GroupInvite.createOf(group, groupOwner, groupMember));
    }

    public static Optional<GroupInvite> groupInvite(Long memberId) {
        return Optional.of(
            GroupInvite.createOf(
                GroupFixture.group(),
                MemberFixture.memberWithMemberId(memberId),
                MemberFixture.memberWithMemberId(memberId + 1)
            )
        );
    }
}
