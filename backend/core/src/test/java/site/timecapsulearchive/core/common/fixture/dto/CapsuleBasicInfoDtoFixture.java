package site.timecapsulearchive.core.common.fixture.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public class CapsuleBasicInfoDtoFixture {

    public static List<CapsuleBasicInfoDto> capsuleBasicInfoDtos(Long capsuleId, int size) {
        List<CapsuleBasicInfoDto> result = new ArrayList<>();
        for (long count = capsuleId; count < size; count++) {
            result.add(new CapsuleBasicInfoDto(count, count + "test-url", ZonedDateTime.now(),
                ZonedDateTime.now(), count + "test-title", false, CapsuleType.GROUP));
        }

        return result;
    }

}
