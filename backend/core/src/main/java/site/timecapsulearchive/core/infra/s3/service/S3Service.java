package site.timecapsulearchive.core.infra.s3.service;

import java.time.Duration;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequestDto;
import site.timecapsulearchive.core.infra.s3.dto.S3PreSignedUrlDto;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class S3Service {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;
    private static final String IMAGE_CONTENT_TYPE = "image/jpeg";
    private static final String VIDEO_CONTENT_TYPE = "video/mp4";

    private final S3UrlGenerator s3UrlGenerator;
    private final S3Presigner s3Presigner;
    private final String temporaryBucketName;

    public S3Service(S3Config s3config, S3UrlGenerator s3UrlGenerator) {
        this.s3Presigner = s3config.s3Presigner();
        this.temporaryBucketName = s3config.getTemporaryBucketName();
        this.s3UrlGenerator = s3UrlGenerator;
    }

    /**
     * 업로드할 파일 이름들을 받아서 서명된 S3 링크를 반환한다.
     *
     * @param memberId 업로드할 유저 아이디
     * @param dto      디렉토리명, 업로드할 이미지, 비디오 파일 이름들
     * @return 서명된 S3 링크
     */
    public S3PreSignedUrlDto getS3PreSignedUrls(
        final Long memberId,
        final S3PreSignedUrlRequestDto dto
    ) {
        Stream<String> imageUrlStream = dto.imageUrls()
            .stream()
            .map(fileName -> createS3PreSignedUrl(
                memberId,
                dto.directory(),
                fileName,
                IMAGE_CONTENT_TYPE
            ));

        Stream<String> videoUrlStream = dto.videoUrls()
            .stream()
            .map(fileName -> createS3PreSignedUrl(
                memberId,
                dto.directory(),
                fileName,
                VIDEO_CONTENT_TYPE
            ));

        return S3PreSignedUrlDto.from(
            Stream.concat(imageUrlStream, videoUrlStream)
                .toList()
        );
    }

    private String createS3PreSignedUrl(
        final Long memberId,
        final String directory,
        final String fileName,
        final String contentType
    ) {
        String newFileName = s3UrlGenerator.generateFileName(memberId, directory, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(temporaryBucketName)
            .key(newFileName)
            .contentType(contentType)
            .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .putObjectRequest(putObjectRequest)
            .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
            putObjectPresignRequest);
        return String.valueOf(presignedPutObjectRequest.url());
    }
}
