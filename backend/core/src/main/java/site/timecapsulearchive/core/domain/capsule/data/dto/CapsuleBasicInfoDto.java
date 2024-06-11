package site.timecapsulearchive.core.domain.capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.data.response.CapsuleBasicInfoResponse;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record CapsuleBasicInfoDto(
    Long capsuleId,
    String skinUrl,
    ZonedDateTime dueDate,
    ZonedDateTime createdAt,
    String title,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public CapsuleBasicInfoResponse toResponse(
        final Function<String, String> singlePreSignUrlFunction) {
        return CapsuleBasicInfoResponse.builder()
            .capsuleId(capsuleId)
            .skinUrl(singlePreSignUrlFunction.apply(skinUrl))
            .dueDate(dueDate)
            .createdAt(createdAt)
            .title(title)
            .isOpened(isOpened)
            .capsuleType(capsuleType)
            .build();
    }
}