package site.timecapsulearchive.core.infra.s3.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Slf4j
@Component
public class S3ObjectManager {

    private final S3Client s3Client;
    private final String bucketName;

    public S3ObjectManager(S3Client s3Client, S3Config s3config) {
        this.s3Client = s3Client;
        this.bucketName = s3config.getBucketName();
    }

    /**
     * s3 상의 경로를 입력한다. (버킷명 제외)
     * <br>ex) capsule/1/test.jpg
     * @param path s3 상에서 경로
     */
    public void deleteObject(String path) {
        try {
            s3Client.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build()
            );
        } catch (AwsServiceException e) {
            log.error("Aws S3에서 삭제 요청 처리 실패 - {}", path, e);
        } catch (SdkClientException e) {
            log.error("Aws S3 삭제 요청 클라이언트 처리 실패 - {}", path, e);
        }
    }
}
