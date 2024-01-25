package site.timecapsulearchive.core.domain.capsuleskin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "캡슐 스킨 요약 정보")
public record CapsuleSkinSummaryResponse(
    @Schema(description = "캡슐 스킨 아이디")

    Long id,

    @Schema(description = "캡슐 스킨 url")

    String skinUrl,

    @Schema(description = "캡슐 스킨 이름")

    String name
) {

}
