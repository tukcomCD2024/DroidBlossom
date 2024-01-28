package site.timecapsulearchive.core.domain.capsule.dto.secret_c;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

@Builder
public record SecretCapsuleCreateRequestDto(
    Long capsuleSkinId,
    String title,
    String content,
    double longitude,
    double latitude,
    ZonedDateTime dueDate,
    List<FileMetaData> fileNames,
    String directory,
    CapsuleType capsuleType
) {

}
