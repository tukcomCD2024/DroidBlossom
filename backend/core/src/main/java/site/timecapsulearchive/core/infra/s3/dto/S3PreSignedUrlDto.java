package site.timecapsulearchive.core.infra.s3.dto;

import java.util.List;

public record S3PreSignedUrlDto(
    List<String> preSignedImageUrls,
    List<String> preSignedVideoUrls
) {

    public static S3PreSignedUrlDto from(
        List<String> preSignedImageUrls,
        List<String> preSignedVideoUrls
    ) {
        return new S3PreSignedUrlDto(preSignedImageUrls, preSignedVideoUrls);
    }
}
