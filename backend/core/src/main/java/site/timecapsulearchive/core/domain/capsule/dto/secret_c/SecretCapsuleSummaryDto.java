package site.timecapsulearchive.core.domain.capsule.dto.secret_c;

import java.time.ZonedDateTime;

public record SecretCapsuleSummaryDto(
    String nickname,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    String address,
    Boolean isOpened,
    ZonedDateTime createdAt
) {

}