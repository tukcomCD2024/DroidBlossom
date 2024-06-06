package site.timecapsulearchive.core.common.fixture.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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

    public static List<GroupCapsuleOpen> groupCapsuleOpensNotAllOpened(
        Capsule capsule,
        List<Member> groupMembers
    ) {
        int mid = groupMembers.size() / 2;
        List<GroupCapsuleOpen> opened = groupCapsuleOpens(true, capsule,
            groupMembers.subList(0, mid));
        List<GroupCapsuleOpen> notOpened = groupCapsuleOpens(false, capsule,
            groupMembers.subList(mid, groupMembers.size() - 1));

        return Stream.of(opened, notOpened)
            .flatMap(Collection::stream)
            .toList();
    }

    public static List<GroupCapsuleOpen> groupCapsuleOpensNotOpenSpecificMemberId(
        Capsule capsule,
        List<Member> groupMembers,
        Long memberId
    ) {
        List<Member> filteredMember = new ArrayList<>();
        Member specificMember = null;
        for (Member member : groupMembers) {
            if (member.getId().equals(memberId)) {
                specificMember = member;
            } else {
                filteredMember.add(member);
            }
        }

        if (specificMember == null) {
            throw new RuntimeException("멤버 리스트에 memberId를 가진 멤버가 존재하지 않습니다.");
        }

        List<GroupCapsuleOpen> result = new ArrayList<>();
        result.add(GroupCapsuleOpen.createOf(specificMember, capsule, false));
        result.addAll(groupCapsuleOpens(true, capsule, filteredMember));

        return result;
    }
}
