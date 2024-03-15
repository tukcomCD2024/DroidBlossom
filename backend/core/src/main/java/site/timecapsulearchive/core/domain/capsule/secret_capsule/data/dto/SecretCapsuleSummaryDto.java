package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto;

import java.time.ZonedDateTime;

public record SecretCapsuleSummaryDto(
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