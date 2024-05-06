package site.timecapsulearchive.core.common.fixture;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
}
