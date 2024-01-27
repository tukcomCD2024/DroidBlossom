package site.timecapsulearchive.core.domain.capsule.dto;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;

public record CapsuleSummaryDto(
    Long id,
    Point point,
    String nickname,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Boolean isOpened
) {

}
