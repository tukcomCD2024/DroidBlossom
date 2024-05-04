package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.MyGroupCapsuleResponse;

public record MyGroupCapsuleDto(
    Long capsuleId,
    String skinUrl,
    ZonedDateTime dueDate,
    ZonedDateTime createdAt,
    String title,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public MyGroupCapsuleResponse toResponse(Function<String, String> singlePreSignUrlFunction) {
        return MyGroupCapsuleResponse.builder()
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
