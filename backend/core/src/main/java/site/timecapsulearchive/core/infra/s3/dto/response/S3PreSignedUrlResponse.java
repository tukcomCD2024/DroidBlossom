package site.timecapsulearchive.core.infra.s3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "미리 서명된 s3 url 응답")
public record S3PreSignedUrlResponse(

    @Schema(description = "미리 서명된 s3 url들")
    List<String> preSignedUrls
) {

    public static S3PreSignedUrlResponse from(List<String> preSignedUrls) {
        return new S3PreSignedUrlResponse(preSignedUrls);
    }
}
