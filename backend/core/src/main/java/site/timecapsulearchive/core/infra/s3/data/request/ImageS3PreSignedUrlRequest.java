package site.timecapsulearchive.core.infra.s3.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;

@Schema(description = "서명된 s3 단건 이미지 url 요청 포맷")
public record ImageS3PreSignedUrlRequest(

    @Schema(description = "이미지 이름")
    @NotBlank(message = "이미지 이름은 필수 입니다.")
    String imageName,

    @Schema(description = "디렉토리")
    @NotNull(message = "디렉토리는 필수 입니다.")
    S3Directory s3Directory
) {

}