package site.timecapsulearchive.core.infra.s3.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "미리 서명된 s3 url 응답")
public record S3PreSignedUrlResponse(

    @Schema(description = "미리 서명된 이미지들의 s3 url들")
    List<String> preSignedImageUrls,

    @Schema(description = "미리 서명된 비디오들의 s3 url들")
    List<String> preSignedVideoUrls
) {

    public static S3PreSignedUrlResponse from(
        final List<String> preSignedImageUrls,
        final List<String> preSignedVideoUrls
    ) {
        return new S3PreSignedUrlResponse(preSignedImageUrls, preSignedVideoUrls);
    }
}
