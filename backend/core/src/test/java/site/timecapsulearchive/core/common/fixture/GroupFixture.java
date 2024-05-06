package site.timecapsulearchive.core.common.fixture;

import site.timecapsulearchive.core.domain.group.entity.Group;

public class GroupFixture {

    /**
     * 그룹 테스트 픽스처를 만든다
     *
     * @return 그룹 테스트 픽스처
     */
    public static Group group() {
        return Group.builder()
            .groupName("test_group")
            .groupDescription("test_group")
            .groupProfileUrl("test_group")
            .build();
    }
}
