package site.timecapsulearchive.core.common.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member.entity.Member;

public class MemberGroupFixture {

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
}
