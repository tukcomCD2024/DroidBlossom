package site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.MyPublicCapsuleResponse;

public record MyPublicCapsuleDto(
    Long capsuleId,
    String skinUrl,
    ZonedDateTime dueDate,
    ZonedDateTime createdAt,
    String title,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public MyPublicCapsuleResponse toResponse(Function<String, String> singlePreSignUrlFunction) {
        return MyPublicCapsuleResponse.builder()
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
