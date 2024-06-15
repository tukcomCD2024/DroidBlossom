package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;

public record GroupCapsuleSummaryDto(
    Long groupId,
    String groupName,
    String groupProfileUrl,
    Long creatorId,
    String creatorNickname,
    String creatorProfileUrl,
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
