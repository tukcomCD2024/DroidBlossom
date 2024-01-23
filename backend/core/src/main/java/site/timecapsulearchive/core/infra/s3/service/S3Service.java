package site.timecapsulearchive.core.infra.s3.service;

import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.dto.response.S3PreSignedUrlResponse;
import site.timecapsulearchive.core.infra.s3.exception.InvalidFileTypeException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3Service {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;

    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3Service(S3Config s3config) {
        this.s3Presigner = s3config.s3Presigner();
        this.bucketName = s3config.getBucketName();
    }

    public S3PreSignedUrlResponse getS3PreSignedUrls(List<String> fileNames) {
        return S3PreSignedUrlResponse.from(fileNames.stream()
            .map(this::createS3PreSignedUrl)
            .toList()
        );
    }

    private String createS3PreSignedUrl(String fileName) {
        PutObjectRequest objectRequest = getObjectRequest(fileName);

        PutObjectPresignRequest preSignRequest = getPreSignRequest(objectRequest);

        PresignedPutObjectRequest preSignedRequest = s3Presigner.presignPutObject(preSignRequest);
        return preSignedRequest.url().toString();
    }

    private PutObjectRequest getObjectRequest(String fileName) {
        return PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(getContentTypeBasedOnExtension(fileName))
            .build();
    }

    private String getContentTypeBasedOnExtension(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        }
        throw new InvalidFileTypeException();
    }

    private PutObjectPresignRequest getPreSignRequest(PutObjectRequest objectRequest) {
        return PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .putObjectRequest(objectRequest)
            .build();
    }


}
