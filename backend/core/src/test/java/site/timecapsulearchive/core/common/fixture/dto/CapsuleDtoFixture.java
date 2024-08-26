package site.timecapsulearchive.core.common.fixture.dto;

import java.time.ZonedDateTime;
import java.util.Optional;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;

public class CapsuleDtoFixture {

    private static final ZonedDateTime now = ZonedDateTime.now();

    public static Optional<PublicCapsuleDetailDto> getPublicCapsuleDetailDto(Long capsuleId,
        Boolean isOpened,
        ZonedDateTime dueDate) {
        return Optional.of(
            new PublicCapsuleDetailDto(capsuleId, "test", dueDate, "testNickname", "testUrl", now,
                null, "testAddress", "testRoadName", "testTitle", "testContent", "testImages",
                "testVideos", isOpened, CapsuleType.PUBLIC, true)
        );
    }

    public static Optional<GroupCapsuleDetailDto> getGroupCapsuleDetailDto(Long groupId,
        Long capsuleId, Boolean isOpened,
        ZonedDateTime dueDate) {
        return Optional.of(
            new GroupCapsuleDetailDto(groupId, capsuleId, "testCapsuleSkinUrl", dueDate, 1L,
                "testNickName", "testProfileUrl",
                null, null, "testAddressName", "testRoadName", "testTitle", "testContent",
                "testImages", "testVideos", isOpened, CapsuleType.PUBLIC)
        );
    }
}