package site.timecapsulearchive.core.common.fixture.dto;

import java.time.ZonedDateTime;
import java.util.Optional;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;

public class CapsuleDetailDtoFixture {

    public static Optional<CapsuleDetailDto> getCapsuleDetailDto(Long capsuleId, Boolean isOpened,
        ZonedDateTime dueDate) {
        ZonedDateTime now = ZonedDateTime.now();

        return Optional.of(
            new CapsuleDetailDto(capsuleId, "test", dueDate, "testNickname", "testUrl", now,
                null, "testAddress", "testRoadName", "testTitle", "testContent", "testImages",
                "testVideos", isOpened, CapsuleType.PUBLIC)
        );
    }


}
