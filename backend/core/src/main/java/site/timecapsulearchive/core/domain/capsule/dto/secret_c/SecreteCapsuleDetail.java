package site.timecapsulearchive.core.domain.capsule.dto.secret_c;

import java.time.ZonedDateTime;

public record SecreteCapsuleDetail(
    Long capsuleId,
    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    String nickname,
    ZonedDateTime createdAt,
    String address,
    String title,
    String content,
    Boolean isOpened
) {

}
