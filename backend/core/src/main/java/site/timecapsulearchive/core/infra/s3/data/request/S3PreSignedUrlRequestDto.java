package site.timecapsulearchive.core.infra.s3.data.request;

import java.util.List;

public record S3PreSignedUrlRequestDto(
    String directory,
    List<String> imageUrls,
    List<String> videoUrls
) {

    public static S3PreSignedUrlRequestDto forPut(
        final String directory,
        final List<String> imageUrls,
        final List<String> videoUrls
    ) {
        return new S3PreSignedUrlRequestDto(directory, imageUrls, videoUrls);
    }

    public static S3PreSignedUrlRequestDto forGet(
        final List<String> imageUrls,
        final List<String> videoUrls
    ) {
        return new S3PreSignedUrlRequestDto("", imageUrls, videoUrls);
    }
}
