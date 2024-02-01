package site.timecapsulearchive.core.domain.capsuleskin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;

@Schema(description = "캡슐 스킨 요약 정보")
@Builder
public record CapsuleSkinSummaryResponse(
    @Schema(description = "캡슐 스킨 아이디")
    Long id,

    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "캡슐 스킨 이름")
    String name,

    @Schema(description = "캡슐 스킨 생성일")
    ZonedDateTime createdAt
) {

}
