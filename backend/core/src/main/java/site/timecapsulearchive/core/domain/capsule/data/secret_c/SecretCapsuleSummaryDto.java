package site.timecapsulearchive.core.domain.capsule.data.secret_c;

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