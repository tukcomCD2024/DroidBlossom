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

    /**
     * 업로드할 파일 이름들을 받아서 서명된 S3 링크를 반환한다.
     * @param memberId 업로드할 유저 아이디
     * @param directory 업로드할 디렉토리
     * @param fileNames 업로드할 파일 이름
     * @return 서명된 S3 링크
     */
    public S3PreSignedUrlResponse getS3PreSignedUrls(
        Long memberId,
        String directory,
        List<String> fileNames
    ) {
        return S3PreSignedUrlResponse.from(
            fileNames.stream()
                .map(fileName -> createS3PreSignedUrl(memberId, directory, fileName))
                .toList()
        );
    }

    private String createS3PreSignedUrl(Long memberId, String directory, String fileName) {
        String newFileName = createNewFileName(memberId, directory, fileName);

        PutObjectRequest putObjectRequest = getPutObjectRequest(newFileName);

        PutObjectPresignRequest putObjectPresignRequest = getPutObjectPreSignRequest(
            putObjectRequest);

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
            putObjectPresignRequest);
        return String.valueOf(presignedPutObjectRequest.url());
    }

    private String createNewFileName(Long memberId, String directory, String filename) {
        return directory + "/" + memberId + "-" + filename;
    }

    private PutObjectRequest getPutObjectRequest(String fileName) {
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

    private PutObjectPresignRequest getPutObjectPreSignRequest(PutObjectRequest objectRequest) {
        return PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .putObjectRequest(objectRequest)
            .build();
    }


}
