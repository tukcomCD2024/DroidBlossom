package site.timecapsulearchive.core.domain.capsule.data.secret_c;

import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

@Builder
public record SecretCapsuleDetailDto(
    Long capsuleId,
    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    String nickname,
    String profileUrl,
    ZonedDateTime createdAt,
    String address,
    String title,
    String content,
    String images,
    String videos,
    Boolean isOpened,
    CapsuleType capsuleType
) {

}
