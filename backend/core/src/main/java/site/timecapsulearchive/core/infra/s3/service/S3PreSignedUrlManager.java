package site.timecapsulearchive.core.infra.s3.service;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.dto.request.S3PreSignedUrlRequestDto;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
public class S3PreSignedUrlManager {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;
    private static final String IMAGE_CONTENT_TYPE = "image/jpeg";
    private static final String VIDEO_CONTENT_TYPE = "video/mp4";

    private final S3UrlGenerator s3UrlGenerator;
    private final S3Presigner s3Presigner;
    private final String temporaryBucketName;
    private final String bucketName;

    public S3PreSignedUrlManager(S3Config s3config, S3UrlGenerator s3UrlGenerator) {
        this.s3Presigner = s3config.s3Presigner();
        this.temporaryBucketName = s3config.getTemporaryBucketName();
        this.bucketName = s3config.getBucketName();
        this.s3UrlGenerator = s3UrlGenerator;
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
                    IMAGE_CONTENT_TYPE)
            ),
            getPreSignedVideoUrls(
                dto,
                fileName -> createS3PreSignedUrlForPut(memberId, dto.directory(), fileName,
                    VIDEO_CONTENT_TYPE)
            )
        );
    }

    private List<String> getPreSignedImageUrls(
        S3PreSignedUrlRequestDto dto,
        Function<String, String> preSignedUrlConvertFunction
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
        S3PreSignedUrlRequestDto dto,
        Function<String, String> preSignedUrlConvertFunction
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

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .getObjectRequest(getObjectRequest)
            .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(
            getObjectPresignRequest);
        return String.valueOf(presignedGetObjectRequest.url());
    }
}
