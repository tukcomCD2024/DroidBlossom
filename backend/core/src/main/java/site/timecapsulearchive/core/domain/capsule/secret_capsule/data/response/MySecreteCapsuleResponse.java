package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
@Schema(description = "내 비밀 캡슐 응답")
public record MySecreteCapsuleResponse(
    @Schema(description = "비밀 캡슐 아이디")
    Long capsuleId,

    @Schema(description = "캡슐 스킨 url")
    String SkinUrl,

    @Schema(description = "내 비밀 캡슐 만료일")
    ZonedDateTime dueDate,

    @Schema(description = "내 비밀 캡슐 생성일")
    ZonedDateTime createdAt,

    @Schema(description = "내 비밀 캡슐 제목")
    String title,

    @Schema(description = "내 비밀 캡슐 오픈 여부")
    Boolean isOpened,

    @Schema(description = "내 비밀 캡슐 타입")
    CapsuleType type
) {

    public MySecreteCapsuleResponse {
        if (dueDate != null) {
            dueDate = dueDate.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }

        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}
