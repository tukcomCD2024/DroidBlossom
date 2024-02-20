package site.timecapsulearchive.core.domain.capsule.dto.mapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.dto.AddressData;
import site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.MySecretCapsulePageResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.MySecreteCapsuleResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformer;

@Component
@RequiredArgsConstructor
public class CapsuleMapper {

    private static final ZoneId ASIA_SEOUL = ZoneId.of("Asia/Seoul");

    private final GeoTransformer geoTransformer;

    public SecretCapsuleCreateRequestDto secretCapsuleCreateRequestToDto(
        SecretCapsuleCreateRequest request) {
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
        SecretCapsuleCreateRequestDto dto,
        Point point,
        Member member,
        CapsuleSkin capsuleSkin
    ) {
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

    private Address addressDataToEntity(AddressData addressData) {
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

    public CapsuleSummaryResponse capsuleSummaryDtoToResponse(CapsuleSummaryDto dto) {
        Point point = geoTransformer.changePoint3857To4326(dto.point());

        return CapsuleSummaryResponse.builder()
            .id(dto.id())
            .latitude(point.getX())
            .longitude(point.getY())
            .nickname(dto.nickname())
            .capsuleSkinUrl(dto.skinUrl())
            .title(dto.title())
            .dueDate(checkNullable(dto.dueDate()))
            .capsuleType(dto.capsuleType())
            .build();
    }

    private ZonedDateTime checkNullable(ZonedDateTime zonedDateTime) {
        if (zonedDateTime != null) {
            return zonedDateTime.withZoneSameInstant(ASIA_SEOUL);
        }
        return null;
    }

    public SecretCapsuleSummaryResponse secretCapsuleSummaryDtoToResponse(
        SecretCapsuleSummaryDto dto) {
        return SecretCapsuleSummaryResponse.builder()
            .nickname(dto.nickname())
            .profileUrl(dto.profileUrl())
            .skinUrl(dto.skinUrl())
            .title(dto.title())
            .dueDate(checkNullable(dto.dueDate()))
            .address(dto.address())
            .roadName(dto.roadName())
            .isOpened(dto.isOpened())
            .createdAt(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .build();
    }

    public SecretCapsuleDetailResponse secretCapsuleDetailDtoToResponse(
        SecretCapsuleDetailDto dto,
        List<String> imageUrls,
        List<String> videoUrls
    ) {
        return SecretCapsuleDetailResponse.builder()
            .capsuleSkinUrl(dto.capsuleSkinUrl())
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

    public SecretCapsuleDetailResponse notOpenedSecretCapsuleDetailDtoToResponse(
        SecretCapsuleDetailDto dto) {
        return SecretCapsuleDetailResponse.builder()
            .capsuleSkinUrl(dto.capsuleSkinUrl())
            .dueDate(checkNullable(dto.dueDate()))
            .nickname(dto.nickname())
            .address(dto.address())
            .isOpened(dto.isOpened())
            .createdDate(dto.createdAt().withZoneSameInstant(ASIA_SEOUL))
            .title(dto.title())
            .build();
    }

    public MySecretCapsulePageResponse capsuleDetailSliceToResponse(
        List<MySecreteCapsuleDto> content,
        boolean hasNext) {
        List<MySecreteCapsuleResponse> responses = content.stream()
            .map(this::mySecreteCapsuleDtoToResponse)
            .toList();

        return new MySecretCapsulePageResponse(
            responses,
            hasNext
        );
    }

    private MySecreteCapsuleResponse mySecreteCapsuleDtoToResponse(MySecreteCapsuleDto dto) {
        return MySecreteCapsuleResponse.builder()
            .capsuleId(dto.capsuleId())
            .SkinUrl(dto.SkinUrl())
            .dueDate(dto.dueDate())
            .createdAt(dto.createdAt())
            .title(dto.title())
            .isOpened(dto.isOpened())
            .type(dto.type())
            .build();
    }

    public AddressData addressEntityToData(Address address) {
        return AddressData.builder()
            .fullRoadAddressName(address.getFullRoadAddressName())
            .province(address.getProvince())
            .city(address.getCity())
            .subDistinct(address.getSubDistinct())
            .roadName(address.getRoadName())
            .mainBuildingNumber(address.getMainBuildingNumber())
            .subBuildingNumber(address.getSubBuildingNumber())
            .buildingName(address.getBuildingName())
            .zipCode(address.getZipCode())
            .build();
    }
}