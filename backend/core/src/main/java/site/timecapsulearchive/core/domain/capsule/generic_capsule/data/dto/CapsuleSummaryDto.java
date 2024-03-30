package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;

public record CapsuleSummaryDto(
    String nickname,
    String profileUrl,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    String address,
    String roadName,
    Boolean isOpened,
    ZonedDateTime createdAt
) {

}