package site.timecapsulearchive.core.domain.capsule.dto;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record CapsuleSummaryDto(
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
