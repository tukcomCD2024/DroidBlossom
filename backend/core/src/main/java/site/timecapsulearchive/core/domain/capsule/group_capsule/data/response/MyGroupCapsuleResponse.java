package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
@Schema(description = "사용자가 만든 그룹 캡슐")
public record MyGroupCapsuleResponse(

    @Schema(description = "캡슐 아이디")
    Long capsuleId,

    @Schema(description = "스킨 url")
    String skinUrl,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "생성일")
    ZonedDateTime createdAt,

    @Schema(description = "제목")
    String title,

    @Schema(description = "캡슐 개봉 여부")
    Boolean isOpened,

    @Schema(description = "캡슐 타입")
    CapsuleType capsuleType
) {

    public MyGroupCapsuleResponse {
        if (dueDate != null) {
            dueDate = dueDate.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }

        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}
