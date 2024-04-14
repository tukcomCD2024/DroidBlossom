package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleSummaryResponse;

public record NearbyCapsuleSummaryDto(
    Long id,
    Point point,
    CapsuleType capsuleType
) {

    public NearbyCapsuleSummaryResponse toResponse() {
        return NearbyCapsuleSummaryResponse.builder()
            .id(id)
            .latitude(point.getX())
            .longitude(point.getY())
            .capsuleType(capsuleType)
            .build();
    }

}
