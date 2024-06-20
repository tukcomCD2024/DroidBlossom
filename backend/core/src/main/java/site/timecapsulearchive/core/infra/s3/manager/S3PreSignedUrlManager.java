package site.timecapsulearchive.core.infra.s3.manager;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.data.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequestDto;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3PreSignedUrlManager {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;
    private static final String IMAGE_CONTENT_TYPE = "image/png";
    private static final String VIDEO_CONTENT_TYPE = "video/mp4";

    private final S3Presigner s3Presigner;
    private final String temporaryBucketName;
    private final String bucketName;

    public S3PreSignedUrlManager(S3Config s3config) {
        this.s3Presigner = s3config.s3Presigner();
        this.temporaryBucketName = s3config.getTemporaryBucketName();
        this.bucketName = s3config.getBucketName();
    }

    /**
     * 업로드할 파일 이름들을 받아서 서명된 S3 링크를 반환한다.
     *
     * @param memberId 업로드할 유저 아이디
     * @param dto      디렉토리명, 업로드할 이미지, 비디오 파일 이름들
     * @return 서명된 S3 링크
     */
    public S3PreSignedUrlDto getS3PreSignedUrlsForPut(
        final Long memberId,
        final S3PreSignedUrlRequestDto dto
    ) {
        return S3PreSignedUrlDto.from(
            getPreSignedImageUrls(
                dto,
                fileName -> createS3PreSignedUrlForPut(memberId, dto.directory(), fileName,
                    IMAGE_CONTENT_TYPE, bucketName)
            ),
            getPreSignedVideoUrls(
                dto,
                fileName -> createS3PreSignedUrlForPut(memberId, dto.directory(), fileName,
                    VIDEO_CONTENT_TYPE, temporaryBucketName)
            )
        );
    }

    public String getImageS3PreSignedUrlsForPut(
        final Long memberId,
        final String directory,
        final String fileName
    ) {
        return createS3PreSignedUrlForPut(memberId, directory, fileName, IMAGE_CONTENT_TYPE,
            bucketName);
    }

    private List<String> getPreSignedImageUrls(
        final S3PreSignedUrlRequestDto dto,
        final UnaryOperator<String> preSignedUrlConvertFunction
    ) {
        if (dto.imageUrls() == null || dto.imageUrls().isEmpty()) {
            return Collections.emptyList();
        }

        return dto.imageUrls()
            .stream()
            .map(preSignedUrlConvertFunction)
            .toList();
    }

    private List<String> getPreSignedVideoUrls(
        final S3PreSignedUrlRequestDto dto,
        final UnaryOperator<String> preSignedUrlConvertFunction
    ) {
        if (dto.videoUrls() == null || dto.videoUrls().isEmpty()) {
            return Collections.emptyList();
        }

        return dto.videoUrls()
            .stream()
            .map(preSignedUrlConvertFunction)
            .toList();
    }

    private String createS3PreSignedUrlForPut(
        final Long memberId,
        final String directory,
        final String fileName,
        final String contentType,
        final String bucketName
    ) {
        final String newFileName = S3UrlGenerator.generateFileName(memberId, directory, fileName);

        final PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .putObjectRequest(builder -> builder
                .bucket(bucketName)
                .key(newFileName)
                .contentType(contentType)
                .build()
            )
            .build();

        final PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
            putObjectPresignRequest);
        return String.valueOf(presignedPutObjectRequest.url());
    }

    public S3PreSignedUrlDto getS3PreSignedUrlsForGet(
        final S3PreSignedUrlRequestDto dto
    ) {
        return S3PreSignedUrlDto.from(
            getPreSignedImageUrls(dto, this::createS3PreSignedUrlForGet),
            getPreSignedVideoUrls(dto, this::createS3PreSignedUrlForGet)
        );
    }

    private String createS3PreSignedUrlForGet(
        final String fileName
    ) {
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

    public String getS3PreSignedUrlForGet(final String fileName) {
        return createS3PreSignedUrlForGet(fileName);
    }

    /**
     * 구분자 ,로 이루어진 히나의 파일 이름들을 받아서 s3 presign url을 반환한다
     *
     * @param fileNames 구분자 ,로 이루어진 하나의 파일 이름들
     * @return 구분자 ,로 구분된 각각의 서명된 이미지 url
     */
    public List<String> getS3PreSignedUrlsForGet(final String fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return Collections.emptyList();
        }

        if (!fileNames.contains(",")) {
            return List.of(
                createS3PreSignedUrlForGet(fileNames)
            );
        }

        return Arrays.stream(fileNames.split(","))
            .filter(url -> !url.isBlank())
            .map(this::createS3PreSignedUrlForGet)
            .toList();
    }
}
