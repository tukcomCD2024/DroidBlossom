package site.timecapsulearchive.core.common.fixture;

import site.timecapsulearchive.core.domain.group.entity.Group;

public class GroupFixture {

    public static Group group() {
        return Group.builder()
            .groupName("test_group")
            .groupDescription("test_group")
            .groupProfileUrl("test_group")
            .build();
    }
}
