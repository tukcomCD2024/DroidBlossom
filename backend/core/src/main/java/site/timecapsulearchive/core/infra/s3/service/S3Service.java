package site.timecapsulearchive.core.infra.s3.service;

import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.FileMetaData;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.dto.response.S3PreSignedUrlResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3Service {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;
    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String location;

    public S3Service(S3Config s3config) {
        this.s3Presigner = s3config.s3Presigner();
        this.bucketName = s3config.getBucketName();
        this.location = s3config.getS3Location();
    }

    /**
     * 업로드할 파일 이름들을 받아서 서명된 S3 링크를 반환한다.
     *
     * @param memberId         업로드할 유저 아이디
     * @param directory        업로드할 디렉토리
     * @param fileMetaDataList 업로드할 파일 메타 데이터 리스트
     * @return 서명된 S3 링크
     */
    public S3PreSignedUrlResponse getS3PreSignedUrls(
        Long memberId,
        String directory,
        List<FileMetaData> fileMetaDataList
    ) {
        return S3PreSignedUrlResponse.from(
            fileMetaDataList.stream()
                .map(fileMetaData -> createS3PreSignedUrl(memberId, directory, fileMetaData))
                .toList()
        );
    }

    private String createS3PreSignedUrl(Long memberId, String directory,
        FileMetaData fileMetaData) {
        String newFileName = createNewFileName(memberId, directory, fileMetaData.fileName());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(newFileName)
            .contentType(fileMetaData.extension())
            .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .putObjectRequest(putObjectRequest)
            .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
            putObjectPresignRequest);
        return String.valueOf(presignedPutObjectRequest.url());
    }

    public String geS3UrlFormat(Long memberId, String directory, String fileName) {
        return "https://"
            + bucketName
            + ".s3."
            + location
            + ".amazonaws.com/"
            + createNewFileName(memberId, directory, fileName);
    }

    private String createNewFileName(Long memberId, String directory, String fileName) {
        return directory + "/" + memberId + "/" + fileName;
    }
}
