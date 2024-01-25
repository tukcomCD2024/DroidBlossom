package site.timecapsulearchive.core.infra.s3.dto.response;

import java.util.List;

public record S3PreSignedUrlResponse(List<String> preSignedUrls) {

    public static S3PreSignedUrlResponse from(List<String> preSignedUrls) {
        return new S3PreSignedUrlResponse(preSignedUrls);
    }
}
