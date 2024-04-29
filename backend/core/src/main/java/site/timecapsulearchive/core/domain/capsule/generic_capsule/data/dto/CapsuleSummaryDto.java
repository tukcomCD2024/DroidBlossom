package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;

public record CapsuleSummaryDto(
    String nickname,
    String profileUrl,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Point point,
    String address,
    String roadName,
    Boolean isOpened,
    ZonedDateTime createdAt
) {

}