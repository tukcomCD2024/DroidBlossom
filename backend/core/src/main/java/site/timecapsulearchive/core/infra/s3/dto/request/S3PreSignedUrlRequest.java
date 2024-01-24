package site.timecapsulearchive.core.infra.s3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "s3업로드 PreSignedURl 포맷")
public record S3PreSignedUrlRequest(

    @Schema(description = "디렉토리 이름")
    @NotBlank(message = "디렉토리 이름은 필수 입니다.")
    String directory,

    @Schema(description = "파일 이름들")
    @NotEmpty(message = "파일 이름은 필수 입니다.")
    List<@NotBlank String> fileNames
) {

}
