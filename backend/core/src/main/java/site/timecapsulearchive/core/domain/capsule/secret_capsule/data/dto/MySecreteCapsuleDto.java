package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecreteCapsuleResponse;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

public record MySecreteCapsuleDto(
    Long capsuleId,
    String skinUrl,
    ZonedDateTime dueDate,
    ZonedDateTime createdAt,
    String title,
    Boolean isOpened,
    CapsuleType type
) {

    public MySecreteCapsuleResponse toResponse(
        final Function<String, String> preSignUrlFunction
    ) {
        return MySecreteCapsuleResponse.builder()
            .capsuleId(capsuleId)
            .SkinUrl(preSignUrlFunction.apply(skinUrl))
            .dueDate(dueDate)
            .createdAt(createdAt)
            .title(title)
            .isOpened(isOpened)
            .type(type)
            .build();
    }

}
