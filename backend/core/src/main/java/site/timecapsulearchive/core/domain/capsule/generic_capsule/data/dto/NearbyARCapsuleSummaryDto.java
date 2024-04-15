package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyARCapsuleSummaryResponse;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

public record NearbyARCapsuleSummaryDto(
    Long id,
    Point point,
    String nickname,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public NearbyARCapsuleSummaryDto {
        if (dueDate != null) {
            dueDate = dueDate.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }

    public NearbyARCapsuleSummaryResponse toResponse(
        final Point point,
        final Function<String, String> preSignUrlFunction
    ) {
        return NearbyARCapsuleSummaryResponse.builder()
            .id(id)
            .latitude(point.getX())
            .longitude(point.getY())
            .nickname(nickname)
            .capsuleSkinUrl(preSignUrlFunction.apply(skinUrl))
            .title(title)
            .dueDate(dueDate)
            .capsuleType(capsuleType)
            .build();
    }
}
