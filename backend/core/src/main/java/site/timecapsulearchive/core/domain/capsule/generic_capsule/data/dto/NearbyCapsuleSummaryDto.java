package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record NearbyCapsuleSummaryDto(
    Long id,
    Point point,
    CapsuleType capsuleType
) {

}
