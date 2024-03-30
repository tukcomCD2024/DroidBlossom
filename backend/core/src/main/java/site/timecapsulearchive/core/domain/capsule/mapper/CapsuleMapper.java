package site.timecapsulearchive.core.domain.capsule.mapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.dto.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecretCapsuleSliceResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.MySecreteCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.secret_capsule.data.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;
import site.timecapsulearchive.core.infra.s3.data.dto.S3PreSignedUrlDto;
import site.timecapsulearchive.core.infra.s3.data.request.S3PreSignedUrlRequestDto;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class CapsuleMapper {

    private static final String DELIMITER = ",";
    private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");

    private final GeoTransformManager geoTransformManager;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    public SecretCapsuleCreateRequestDto secretCapsuleCreateRequestToDto(
        final SecretCapsuleCreateRequest request) {
        return SecretCapsuleCreateRequestDto.builder()
            .capsuleSkinId(request.capsuleSkinId())
            .title(request.title())
            .content(request.content())
            .longitude(request.longitude())
            .latitude(request.latitude())
            .addressData(request.addressData())
            .dueDate(request.dueDate())
            .imageNames(request.imageNames())
            .videoNames(request.videoNames())
            .directory(request.directory())
            .build();
    }

    public Capsule requestDtoToEntity(
        final SecretCapsuleCreateRequestDto dto,
        final Member member,
        final CapsuleSkin capsuleSkin
    ) {
        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        return Capsule.builder()
            .title(dto.title())
            .content(dto.content())
            .point(point)
            .address(addressDataToEntity(dto.addressData()))
            .type(CapsuleType.SECRET)
            .member(member)
            .dueDate(dto.dueDate())
            .capsuleSkin(capsuleSkin)
            .build();
    }

    private Address addressDataToEntity(final AddressData addressData) {
        return Address.builder()
            .fullRoadAddressName(addressData.fullRoadAddressName())
            .province(addressData.province())
            .city(addressData.city())
            .subDistinct(addressData.subDistinct())
            .roadName(addressData.roadName())
            .mainBuildingNumber(addressData.mainBuildingNumber())
            .subBuildingNumber(addressData.subBuildingNumber())
            .buildingName(addressData.buildingName())
            .zipCode(addressData.zipCode())
            .build();
    }

    public NearbyCapsuleSummaryResponse capsuleSummaryDtoToResponse(final NearbyCapsuleSummaryDto dto) {
        Point point = geoTransformManager.changePoint3857To4326(dto.point());

        return NearbyCapsuleSummaryResponse.builder()
            .id(dto.id())
            .latitude(point.getX())
            .longitude(point.getY())
            .nickname(dto.nickname())
            .capsuleSkinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.skinUrl()))
            .title(dto.title())
            .dueDate(checkNullable(dto.dueDate()))
            .capsuleType(dto.capsuleType())
            .build();
    }

    private ZonedDateTime checkNullable(final ZonedDateTime zonedDateTime) {
        if (zonedDateTime != null) {
            return zonedDateTime.withZoneSameInstant(ASIA_SEOUL);
        }
        return null;
    }

    public CapsuleSummaryResponse secretCapsuleSummaryDtoToResponse(
        final SecretCapsuleSummaryDto dto) {
        return CapsuleSummaryResponse.builder()
            .nickname(dto.nickname())
            .profileUrl(dto.profileUrl())
            .skinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.skinUrl()))
            .title(dto.title())
            .dueDate(checkNullable(dto.dueDate()))
            .address(dto.address())
            .roadName(dto.roadName())
            .isOpened(dto.isOpened())
            .createdAt(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .build();
    }

    public CapsuleDetailResponse secretCapsuleDetailDtoToResponse(
        final SecretCapsuleDetailDto dto,
        final List<String> imageUrls,
        final List<String> videoUrls
    ) {
        return CapsuleDetailResponse.builder()
            .capsuleSkinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.capsuleSkinUrl()))
            .dueDate(checkNullable(dto.dueDate()))
            .nickname(dto.nickname())
            .profileUrl(dto.profileUrl())
            .createdDate(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .address(dto.address())
            .title(dto.title())
            .content(dto.content())
            .imageUrls(imageUrls)
            .videoUrls(videoUrls)
            .isOpened(dto.isOpened())
            .capsuleType(dto.capsuleType())
            .build();
    }

    public CapsuleDetailResponse notOpenedSecretCapsuleDetailDtoToResponse(
        final SecretCapsuleDetailDto dto) {
        return CapsuleDetailResponse.builder()
            .capsuleSkinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.capsuleSkinUrl()))
            .dueDate(checkNullable(dto.dueDate()))
            .nickname(dto.nickname())
            .address(dto.address())
            .isOpened(dto.isOpened())
            .createdDate(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .title(dto.title())
            .build();
    }

    public MySecretCapsuleSliceResponse capsuleDetailSliceToResponse(
        final List<MySecreteCapsuleDto> content,
        final boolean hasNext
    ) {
        List<MySecreteCapsuleResponse> responses = content.stream()
            .map(this::mySecreteCapsuleDtoToResponse)
            .toList();

        return new MySecretCapsuleSliceResponse(
            responses,
            hasNext
        );
    }

    private MySecreteCapsuleResponse mySecreteCapsuleDtoToResponse(final MySecreteCapsuleDto dto) {
        return MySecreteCapsuleResponse.builder()
            .capsuleId(dto.capsuleId())
            .SkinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.skinUrl()))
            .dueDate(dto.dueDate())
            .createdAt(dto.createdAt())
            .title(dto.title())
            .isOpened(dto.isOpened())
            .type(dto.type())
            .build();
    }

    public CapsuleDetailResponse publicCapsuleDetailDtoToResponse(
        PublicCapsuleDetailDto detailDto
    ) {
        final S3PreSignedUrlDto preSignedUrls = s3PreSignedUrlManager.getS3PreSignedUrlsForGet(
            S3PreSignedUrlRequestDto.forGet(
                splitFileNames(detailDto.images()),
                splitFileNames(detailDto.videos())
            )
        );

        return CapsuleDetailResponse.builder()
            .capsuleSkinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(detailDto.capsuleSkinUrl()))
            .dueDate(checkNullable(detailDto.dueDate()))
            .nickname(detailDto.nickname())
            .profileUrl(detailDto.profileUrl())
            .createdDate(detailDto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .address(detailDto.address())
            .title(detailDto.title())
            .content(detailDto.content())
            .imageUrls(preSignedUrls.preSignedImageUrls())
            .videoUrls(preSignedUrls.preSignedVideoUrls())
            .isOpened(detailDto.isOpened())
            .capsuleType(detailDto.capsuleType())
            .build();
    }

    private List<String> splitFileNames(String fileNames) {
        if (fileNames == null) {
            return Collections.emptyList();
        }

        return List.of(fileNames.split(DELIMITER));
    }

    public CapsuleDetailResponse notOpenedPublicCapsuleDetailDtoToResponse(
        final PublicCapsuleDetailDto dto
    ) {
        return CapsuleDetailResponse.builder()
            .capsuleSkinUrl(s3PreSignedUrlManager.getS3PreSignedUrlForGet(dto.capsuleSkinUrl()))
            .dueDate(checkNullable(dto.dueDate()))
            .nickname(dto.nickname())
            .profileUrl(dto.profileUrl())
            .address(dto.address())
            .isOpened(dto.isOpened())
            .createdDate(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .imageUrls(Collections.emptyList())
            .videoUrls(Collections.emptyList())
            .title(dto.title())
            .capsuleType(dto.capsuleType())
            .build();
    }
}
