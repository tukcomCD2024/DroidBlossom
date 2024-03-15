package site.timecapsulearchive.core.infra.s3.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;
import site.timecapsulearchive.core.global.common.valid.annotation.Video;

@Schema(description = "s3업로드 PreSignedURl 포맷")
public record S3PreSignedUrlRequest(

    @Schema(description = "디렉토리 이름")
    @NotBlank(message = "디렉토리 이름은 필수 입니다.")
    String directory,

    @Schema(description = "파일 이름들", requiredMode = RequiredMode.NOT_REQUIRED)
    List<@Image String> imageNames,

    @Schema(description = "비디오 파일 이름들", requiredMode = RequiredMode.NOT_REQUIRED)
    List<@Video String> videoNames
) {

}
