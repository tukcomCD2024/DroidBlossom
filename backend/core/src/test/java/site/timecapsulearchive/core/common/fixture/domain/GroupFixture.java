package site.timecapsulearchive.core.common.fixture.domain;

import java.util.List;
import java.util.stream.IntStream;
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

    public static List<Group> groups(int startDataPrefix, int count) {
        return IntStream.range(startDataPrefix, count)
            .mapToObj(idx -> Group.builder()
                .groupName(idx + "test_group_name")
                .groupDescription(idx + "test_group_description")
                .groupProfileUrl(idx + "test+group_profile_url")
                .build())
            .toList();
    }
}
