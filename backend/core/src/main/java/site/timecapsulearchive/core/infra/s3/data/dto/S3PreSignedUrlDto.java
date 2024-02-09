package site.timecapsulearchive.core.infra.s3.data.dto;

import java.util.List;

public record S3PreSignedUrlDto(
    List<String> preSignedImageUrls,
    List<String> preSignedVideoUrls
) {

    public static S3PreSignedUrlDto from(
        final List<String> preSignedImageUrls,
        final List<String> preSignedVideoUrls
    ) {
        return new S3PreSignedUrlDto(preSignedImageUrls, preSignedVideoUrls);
    }
}
