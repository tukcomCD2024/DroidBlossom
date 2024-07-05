package site.timecapsulearchive.core.common.fixture.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class GroupCapsuleOpenFixture {

    public static List<GroupCapsuleOpen> groupCapsuleOpens(
        Group group,
        Boolean isOpened,
        Capsule capsule,
        List<Member> groupMembers
    ) {
        return groupMembers.stream()
            .map(member -> getGroupCapsuleOpen(group, isOpened, capsule, member))
            .toList();
    }

    private static GroupCapsuleOpen getGroupCapsuleOpen(
        Group group,
        Boolean isOpened,
        Capsule capsule,
        Member member
    ) {
        try {
            Constructor<GroupCapsuleOpen> declaredConstructor = GroupCapsuleOpen.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);

            GroupCapsuleOpen groupCapsuleOpen = declaredConstructor.newInstance();
            setFieldValue(groupCapsuleOpen, "group", group);
            setFieldValue(groupCapsuleOpen, "capsule", capsule);
            setFieldValue(groupCapsuleOpen, "isCapsuleOpened", isOpened);
            setFieldValue(groupCapsuleOpen, "member", member);
            return groupCapsuleOpen;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setFieldValue(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GroupCapsuleOpen> groupCapsuleOpensNotAllOpened(
        Group group,
        Capsule capsule,
        List<Member> groupMembers
    ) {
        int mid = groupMembers.size() / 2;
        List<GroupCapsuleOpen> opened = groupCapsuleOpens(group, true, capsule,
            groupMembers.subList(0, mid));
        List<GroupCapsuleOpen> notOpened = groupCapsuleOpens(group, false, capsule,
            groupMembers.subList(mid, groupMembers.size() - 1));

        return Stream.of(opened, notOpened)
            .flatMap(Collection::stream)
            .toList();
    }

    public static List<GroupCapsuleOpen> groupCapsuleOpensNotOpenSpecificMemberId(
        Group group,
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
        result.add(getGroupCapsuleOpen(group, false, capsule, specificMember));
        result.addAll(groupCapsuleOpens(group, true, capsule, filteredMember));

        return result;
    }
}
