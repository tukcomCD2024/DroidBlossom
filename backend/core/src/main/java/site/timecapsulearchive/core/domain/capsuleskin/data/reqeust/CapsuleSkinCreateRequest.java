package site.timecapsulearchive.core.domain.capsuleskin.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;

@Schema(description = "캡슐 스킨 생성 포맷")
public record CapsuleSkinCreateRequest(

    @Schema(description = "캡슐 스킨 이름")
    @NotBlank(message = "캡슐 스킨 이름은 필수 입니다.")
    String skinName,

    @Schema(description = "캡슐 스킨 사진 URL")
    @Image @NotBlank(message = "캡슐 스킨 사진 URL은 필수 입니다.")
    String imageUrl,

    @Schema(description = "디렉토리 이름")
    @NotBlank(message = "디렉토리는 필수입니다.")
    String directory,

    @Schema(description = "캡슐 스킨 모션 이름")
    String motionName
) {

}