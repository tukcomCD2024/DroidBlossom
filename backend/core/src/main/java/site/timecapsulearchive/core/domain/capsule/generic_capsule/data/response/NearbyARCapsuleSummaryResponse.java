package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Schema(description = "AR 캡슐 요약 정보")
@Builder
public record NearbyARCapsuleSummaryResponse(

    @Schema(description = "캡슐 아이디")
    Long id,

    @Schema(description = "캡슐 위도 좌표")
    Double latitude,

    @Schema(description = "캡슐 경도 좌표")
    Double longitude,

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "캡슐 스킨 url")
    String capsuleSkinUrl,

    @Schema(description = "제목")
    String title,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 타입")
    CapsuleType capsuleType
) {

    public NearbyARCapsuleSummaryResponse {
        if (dueDate != null) {
            dueDate = dueDate.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}