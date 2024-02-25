package site.timecapsulearchive.notification.infra.s3;

import java.time.Duration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
public class S3PreSignedUrlManager {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;

    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3PreSignedUrlManager(S3Config s3config) {
        this.s3Presigner = s3config.s3Presigner();
        this.bucketName = s3config.getBucketName();
    }

    public String createS3PreSignedUrlForGet(final String fileName) {
        final GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .getObjectRequest(builder -> builder
                .bucket(bucketName)
                .key(fileName)
                .build()
            )
            .build();

        final PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
            getObjectPresignRequest);
        return String.valueOf(presignedGetObjectRequest.url());
    }
}
