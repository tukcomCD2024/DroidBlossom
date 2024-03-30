package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record NearbyCapsuleSummaryDto(
    Long id,
    Point point,
    String nickname,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Boolean isOpened,
    CapsuleType capsuleType
) {

}
