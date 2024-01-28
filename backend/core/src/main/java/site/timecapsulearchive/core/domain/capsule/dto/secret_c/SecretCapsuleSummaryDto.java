package site.timecapsulearchive.core.domain.capsule.dto.secret_c;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;

public record SecretCapsuleSummaryDto(
    Long id,
    Point point,
    String nickname,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    String address,
    Boolean isOpened,
    ZonedDateTime createdAt
) {

}