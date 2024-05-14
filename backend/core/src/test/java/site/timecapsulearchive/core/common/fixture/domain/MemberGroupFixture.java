package site.timecapsulearchive.core.common.fixture.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class MemberGroupFixture {

    /**
     * 테스트 픽스처로 그룹장으로 그룹 멤버를 생성한다
     *
     * @param member 그룹장이 될 멤버
     * @param group 대상 그룹
     * @return 그룹 멤버 테스트 픽스처
     */
    public static MemberGroup memberGroup(Member member, Group group) {
        return MemberGroup.createGroupOwner(member, group);
    }

    /**
     * 테스트 픽스처 - 사용자, 그룹, 그룹장 여부를 받아 그룹원을 만들어준다.
     * @param member 사용자
     * @param group 그룹
     * @param isOwner 그룹장 여부
     * @return MemberGroup 테스트 픽스처
     */
    public static MemberGroup memberGroup(Member member, Group group, Boolean isOwner) {
        try {
            Constructor<MemberGroup> declaredConstructor = MemberGroup.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);

            MemberGroup memberGroup = declaredConstructor.newInstance();
            setFieldValue(memberGroup, "member", member);
            setFieldValue(memberGroup, "group", group);
            setFieldValue(memberGroup, "isOwner", isOwner);

            return memberGroup;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setFieldValue(Object instance, String fieldName, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * 테스트 픽스처 - 그룹원들과 그룹을 주면 그룹원들 목록을 만들어준다(그룹장 X)
     * @param members 그룹원들 목록
     * @param group 그룹
     * @return {@code List<MemberGroup>} 테스트 픽스처들
     */
    public static List<MemberGroup> memberGroups(List<Member> members, Group group) {
        return members.stream()
            .map(m -> memberGroup(m, group, Boolean.FALSE))
            .toList();
    }
}
