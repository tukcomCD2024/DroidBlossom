package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureOpenStatus;

@Schema(name = "보물 캡슐 개봉 응답")
public record TreasureCapsuleOpenResponse(
    @Schema(name = "보물 개봉 상태")
    TreasureOpenStatus treasureOpenStatus,

    @Schema(name = "보물 이미지 URL")
    String treasureImageUrl
) {

}
