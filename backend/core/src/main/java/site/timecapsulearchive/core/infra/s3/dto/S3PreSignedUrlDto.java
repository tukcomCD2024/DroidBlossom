package site.timecapsulearchive.core.infra.s3.dto;

import java.util.List;

public record S3PreSignedUrlDto(List<String> preSignedUrls) {

    public static S3PreSignedUrlDto from(List<String> preSignedUrls) {
        return new S3PreSignedUrlDto(preSignedUrls);
    }
}
