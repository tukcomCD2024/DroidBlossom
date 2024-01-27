package site.timecapsulearchive.core.domain.capsule.dto.secret_c.mapper;

import java.time.ZonedDateTime;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.FileMetaData;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record CapsuleCreateRequestDto(
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
