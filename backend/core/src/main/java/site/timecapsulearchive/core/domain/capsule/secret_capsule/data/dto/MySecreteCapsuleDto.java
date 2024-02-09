package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record MySecreteCapsuleDto(
    Long capsuleId,
    String SkinUrl,
    ZonedDateTime dueDate,
    ZonedDateTime createdAt,
    String title,
    Boolean isOpened,
    CapsuleType type
) {

}
