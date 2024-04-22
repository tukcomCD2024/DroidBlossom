package site.timecapsulearchive.core.domain.capsuleskin.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Motion;
import site.timecapsulearchive.core.domain.capsuleskin.entity.Retarget;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;

@Schema(description = "캡슐 스킨 생성 포맷")
public record CapsuleSkinCreateRequest(

    @Schema(description = "캡슐 스킨 이름")
    @NotBlank(message = "캡슐 스킨 이름은 필수 입니다.")
    String skinName,

    @Schema(description = "캡슐 스킨 이미지 파일명")
    @Image @NotBlank(message = "캡슐 스킨 이미지 파일명은 필수 입니다.")
    String imageName,

    @Nullable
    @Schema(description = "캡슐 스킨 모션 이름")
    Motion motionName,

    @Nullable
    @Schema(description = "캡슐 스킨 구조")
    Retarget retarget
) {

    public CapsuleSkinCreateDto toCapsuleSkinCreateDto(String imageFullPath) {
        return CapsuleSkinCreateDto.builder()
            .skinName(skinName)
            .imageFullPath(imageFullPath)
            .motionName(motionName)
            .retarget(retarget)
            .build();
    }
}