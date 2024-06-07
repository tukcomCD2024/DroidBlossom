package site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "오픈할 보물 캡슐 요청 포맷")
public record OpenTreasureCapsuleRequest(

    @Schema(name = "보물 캡슐 아이디")
    Long treasureCapsuleId,

    @Schema(name = "보물 캡슐 스킨 Gif")
    String treasureCapsuleSkinGif

) {

}
