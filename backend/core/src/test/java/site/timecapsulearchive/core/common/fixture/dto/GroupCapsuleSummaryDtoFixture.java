package site.timecapsulearchive.core.common.fixture.dto;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.common.dependency.UnitTestDependency;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;

public class GroupCapsuleSummaryDtoFixture {

    private static final double TEST_LATITUDE = 37.078;
    private static final double TEST_LONGITUDE = 127.423;

    public static GroupCapsuleSummaryDto groupCapsule(Long groupId) {
        return new GroupCapsuleSummaryDto(
            groupId,
            "test-group",
            "test-group-profile.com",
            "test-creator",
            "test-creator-profile.com",
            "test-capsule-skin.com",
            "test-capsule-title",
            ZonedDateTime.now(),
            UnitTestDependency.geoTransformManager()
                .changePoint4326To3857(TEST_LATITUDE, TEST_LONGITUDE),
            "test-address",
            "test-roadName",
            false,
            ZonedDateTime.now()
        );
    }
}
