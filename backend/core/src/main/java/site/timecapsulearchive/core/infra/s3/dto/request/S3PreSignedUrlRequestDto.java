package site.timecapsulearchive.core.infra.s3.dto.request;

import java.util.List;

public record S3PreSignedUrlRequestDto(
    String directory,
    List<String> imageUrls,
    List<String> videoUrls
) {

    public static S3PreSignedUrlRequestDto from(
        String directory,
        List<String> imageUrls,
        List<String> videoUrls
    ) {
        return new S3PreSignedUrlRequestDto(directory, imageUrls, videoUrls);
    }
}