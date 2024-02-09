package site.timecapsulearchive.core.domain.capsuleskin.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "캡슐 스킨 업데이트 포맷")
public record CapsuleSkinUpdateRequest(
    @Schema(description = "캡슐 스킨 이름")

    String name
) {

}