package site.timecapsulearchive.core.common.fixture.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;

public class CapsuleDtoFixture {

    private static ZonedDateTime now = ZonedDateTime.now();

    public static Optional<CapsuleDetailDto> getCapsuleDetailDto(Long capsuleId, Boolean isOpened,
        ZonedDateTime dueDate) {
        return Optional.of(
            new CapsuleDetailDto(capsuleId, "test", dueDate, "testNickname", "testUrl", now,
                null, "testAddress", "testRoadName", "testTitle", "testContent", "testImages",
                "testVideos", isOpened, CapsuleType.PUBLIC)
        );
    }

    public static Optional<GroupCapsuleDetailDto> getGroupCapsuleDetailDto(Long capsuleId,
        boolean isOpened, ZonedDateTime now, int count) {
        return Optional.of(new GroupCapsuleDetailDto(getCapsuleDetailDto(capsuleId, isOpened, now).get(),
            getGroupMemberSummaryDtos(count)));
    }

    private static List<GroupMemberSummaryDto> getGroupMemberSummaryDtos(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new GroupMemberSummaryDto(i + "testNickname", i + "testUrl", true))
            .toList();
    }
}