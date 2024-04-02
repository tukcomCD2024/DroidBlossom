package site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto;

import java.time.ZonedDateTime;

public record PublicCapsuleSummaryDto(
    String nickname,
    String profileUrl,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    String address,
    String roadName,
    Boolean isOpened,
    ZonedDateTime createdAt,
    Boolean isFriend
) {

}