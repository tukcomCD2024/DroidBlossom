package site.timecapsulearchive.core.infra.s3.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;
import site.timecapsulearchive.core.global.common.valid.annotation.Video;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;

@Schema(description = "서명된 s3 이미지, 비디오 url 요청 포맷")
public record S3PreSignedUrlRequest(

    @Schema(description = "디렉토리")
    @NotNull(message = "디렉토리는 필수 입니다.")
    S3Directory directory,

    @Schema(description = "이미지 파일 이름들")
    @Size(max = 20)
    List<@Image String> imageNames,

    @Schema(description = "비디오 파일 이름들")
    @Size(max = 20)
    List<@Video String> videoNames
) {

}
