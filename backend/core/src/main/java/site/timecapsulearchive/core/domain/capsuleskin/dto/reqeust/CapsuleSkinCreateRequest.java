package site.timecapsulearchive.core.domain.capsuleskin.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "캡슐 스킨 생성 포맷")
@Validated
public record CapsuleSkinCreateRequest(

    @Schema(description = "캡슐 스킨 이름")
    String name,

    @Schema(description = "캡슐 스킨 이미지")
    MultipartFile skinImage,

    @Schema(description = "캡슐 스킨 모션 이름")
    String motionName
) {

}