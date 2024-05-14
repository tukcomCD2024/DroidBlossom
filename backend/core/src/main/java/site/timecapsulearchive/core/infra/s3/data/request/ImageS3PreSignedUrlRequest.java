package site.timecapsulearchive.core.infra.s3.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "단일 이미지 s3업로드 PreSignedURl 포맷")
public record ImageS3PreSignedUrlRequest(

    @Schema(description = "이미지 이름")
    @NotBlank(message = "이미지 이름은 필수 입니다.")
    String imageName,

    @Schema(description = "디렉토리 이름")
    @NotBlank(message = "디렉토리 이름은 필수 입니다.")
    String directory

) {

}