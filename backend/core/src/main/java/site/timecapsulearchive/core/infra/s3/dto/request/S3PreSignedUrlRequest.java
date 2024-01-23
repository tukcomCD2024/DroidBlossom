package site.timecapsulearchive.core.infra.s3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "s3업로드 PreSignedURl 포맷")
public record S3PreSignedUrlRequest(
    @Schema(description = "파일 이름들")
    @NotEmpty(message = "파일 이름은 필수 입니다.")
    List<String> fileNames
) {

}
