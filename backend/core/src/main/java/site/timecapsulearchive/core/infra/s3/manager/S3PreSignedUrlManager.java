package site.timecapsulearchive.core.infra.s3.manager;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.data.dto.S3Directory;
import site.timecapsulearchive.core.infra.s3.data.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequestDto;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

/**
 * s3 이미지에 대한 path를 받아서 서명된 링크를 돌려주는 클래스
 */
@Component
public class S3PreSignedUrlManager {

    private static final long PRE_SIGNED_URL_EXPIRATION_TIME = 10;
    private static final String DELIMITER = ",";
    private static final String IMAGE_CONTENT_TYPE = "image/png";
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
     * 업로드할 이미지 이름들을 받아서 서명된 s3 링크들을 반환한다.
     * <p>
     * 1. 회원 아이디와 이미지 이름로 path를 만든다.
     * <p>
     * 2. 위에서 만든 path에 put 할 수 있는 서명된 링크들을 만든다.
     *
     * @param s3Directory 디렉토리
     * @param memberId    회원 아이디
     * @param imageNames  이미지 파일명들 ex) ["image.png", "example.png"]
     * @return 서명된 이미지 업로드 링크들
     */
    public List<String> preSignMultipleImagesForPut(
        final S3Directory s3Directory,
        final Long memberId,
        final List<String> imageNames
    ) {
        return imageNames.stream()
            .filter(path -> !path.isBlank())
            .map(path -> s3Directory.generateFullPath(memberId, path))
            .map(fullPath -> createS3PreSignedUrlForPut(bucketName, fullPath, IMAGE_CONTENT_TYPE))
            .toList();
    }

    private String createS3PreSignedUrlForPut(
        final String bucketName,
        final String fullPath,
        final String contentType
    ) {
        final PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(PRE_SIGNED_URL_EXPIRATION_TIME))
            .putObjectRequest(builder -> builder
                .bucket(bucketName)
                .key(fullPath)
                .contentType(contentType)
                .build()
            )
            .build();

        final PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(
            putObjectPresignRequest);
        return String.valueOf(presignedPutObjectRequest.url());
    }

    /**
     * 업로드할 비디오 이름들을 받아서 서명된 s3 링크들을 반환한다.
     * <p>
     * 1. 회원 아이디와 비디오 이름으로 path를 만든다.
     * <p>
     * 2. 위에서 만든 path에 put 할 수 있는 서명된 링크들을 만든다.
     *
     * @param s3Directory 디렉토리
     * @param memberId    회원 아이디
     * @param videoNames  비디오 파일명들 ex) ["image.mp4", "example.mp4"]
     * @return 서명된 비디오 업로드 링크들
     */
    public List<String> preSignMultipleVideosForPut(
        final S3Directory s3Directory,
        final Long memberId,
        final List<String> videoNames
    ) {
        return videoNames.stream()
            .filter(path -> !path.isBlank())
            .map(path -> s3Directory.generateFullPath(memberId, path))
            .map(fullPath -> createS3PreSignedUrlForPut(temporaryBucketName, fullPath,
                VIDEO_CONTENT_TYPE))
            .toList();
    }

    /**
     * 업로드할 이미지 이름을 받아서 서명된 s3 링크를 반환한다.
     * <p>
     * 1. 회원 아이디와 이미지 이름로 path를 만든다.
     * <p>
     * 2. 위에서 만든 path에 put 할 수 있는 서명된 링크를 만든다.
     *<p>
     *이미지 경로가 null이거나 공백인 경우 공백을 반환한다.
     *
     * @param s3Directory 디렉토리
     * @param memberId    회원 아이디
     * @param imageName  이미지 파일명 ex) image.png
     * @return 서명된 이미지 업로드 링크들
     */
    public String preSignSingleImageForPut(
        final S3Directory s3Directory,
        final Long memberId,
        final String imageName
    ) {
        if (imageName.isBlank()) {
            return "";
        }

        final String fullPath = s3Directory.generateFullPath(memberId, imageName);

        return createS3PreSignedUrlForPut(bucketName, fullPath, IMAGE_CONTENT_TYPE);
    }

    /**
     * 구분자 ,로 이루어진 완전한 이미지 경로명들을 받아서 서명된 s3 링크들을 반환한다.
     * <p>
     * 1. 파일 이름이 공백이거나 null인 경우 빈 리스트를 반환한다.
     * <p>
     * 2. 구분자를 포함하지 않은 경우 입력 전체를 서명한다.
     * <p>
     * 3. 구분자를 분리하며 분리된 path를 서명한다
     *
     * @param filePaths 구분자 ,로 이루어진 완전한 이미지 경로명들
     * @return 구분자 ,로 구분된 각각의 서명된 s3 링크들
     */
    public List<String> preSignImagesForGet(final String filePaths) {
        if (filePaths.isBlank()) {
            return Collections.emptyList();
        }

        if (!filePaths.contains(DELIMITER)) {
            return List.of(
                createS3PreSignedUrlForGet(filePaths)
            );
        }

        return Arrays.stream(filePaths.split(DELIMITER))
            .filter(path -> !path.isBlank())
            .map(this::createS3PreSignedUrlForGet)
            .toList();
    }

    /**
     * 완전한 이미지 경로명을 받아서 서명된 s3 링크를 반환한다.
     * <p>
     * 1. 파일 이름이 공백이거나 null인 경우 빈 문자열을 반환한다.
     * <p>
     * 2. 이미지 경로를 서명한다
     *
     * @param imagePath 완전한 이미지 경로명
     * @return <code>imagePath</code>로 서명된 s3 링크
     */
    public String preSignImageForGet(final String imagePath) {
        if (imagePath.isBlank()) {
            return "";
        }

        return createS3PreSignedUrlForGet(imagePath);
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
        final String newFileName = s3UrlGenerator.generateFileName(memberId, directory, fileName);

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
        if (fileNames == null) {
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
