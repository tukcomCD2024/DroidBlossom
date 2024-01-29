package site.timecapsulearchive.core.domain.capsule.dto.mapper;

import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecreteCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleDetailResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.response.SecretCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.global.geography.GeoTransformer;

@Component
@RequiredArgsConstructor
public class CapsuleMapper {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    private final GeoTransformer geoTransformer;

    public SecretCapsuleCreateRequestDto secretCapsuleCreateRequestToDto(
        SecretCapsuleCreateRequest request) {
        return SecretCapsuleCreateRequestDto.builder()
            .capsuleSkinId(request.capsuleSkinId())
            .title(request.title())
            .content(request.content())
            .longitude(request.longitude())
            .latitude(request.latitude())
            .dueDate(request.dueDate())
            .fileNames(request.fileNames())
            .directory(request.directory())
            .capsuleType(request.capsuleType())
            .build();
    }

    public Capsule requestDtoToEntity(
        SecretCapsuleCreateRequestDto dto,
        Point point,
        Address address,
        Member member,
        CapsuleSkin capsuleSkin
    ) {
        return Capsule.builder()
            .title(dto.title())
            .content(dto.content())
            .point(point)
            .address(address)
            .type(dto.capsuleType())
            .member(member)
            .dueDate(dto.dueDate())
            .capsuleSkin(capsuleSkin)
            .build();
    }

    public CapsuleSummaryResponse capsuleSummaryDtoToResponse(CapsuleSummaryDto dto) {
        Point point = geoTransformer.changePoint3857To4326(dto.point());

        return CapsuleSummaryResponse.builder()
            .id(dto.id())
            .longitude(point.getX())
            .latitude(point.getY())
            .nickname(dto.nickname())
            .capsuleSkinUrl(dto.skinUrl())
            .title(dto.title())
            .dueDate(dto.dueDate().withZoneSameInstant(ZONE_ID))
            .capsuleType(dto.capsuleType())
            .build();
    }


    public SecretCapsuleSummaryResponse secretCapsuleSummaryDtoToResponse(
        SecretCapsuleSummaryDto dto) {
        return SecretCapsuleSummaryResponse.builder()
            .nickname(dto.nickname())
            .skinUrl(dto.skinUrl())
            .title(dto.title())
            .dueDate(dto.dueDate().withZoneSameInstant(ZONE_ID))
            .address(dto.address())
            .isOpened(dto.isOpened())
            .createdAt(dto.createdAt().withZoneSameInstant(ZONE_ID))
            .build();
    }

    public SecretCapsuleDetailResponse secretCapsuleDetailDtoToResponse(
        SecreteCapsuleDetailDto dto) {
        return SecretCapsuleDetailResponse.builder()
            .capsuleSkinUrl(dto.capsuleSkinUrl())
            .dueDate(dto.dueDate().withZoneSameInstant(ZONE_ID))
            .nickname(dto.nickname())
            .createdDate(dto.createdAt().withZoneSameInstant(ZONE_ID))
            .address(dto.address())
            .title(dto.title())
            .content(dto.content())
            .imageUrls(dto.images())
            .videoUrls(dto.videos())
            .isOpened(dto.isOpened())
            .build();
    }

    public SecretCapsuleDetailResponse notOpenedSecreteCapsuleDetailDtoToResponse(
        SecreteCapsuleDetailDto dto) {
        return SecretCapsuleDetailResponse.builder()
            .capsuleSkinUrl(dto.capsuleSkinUrl())
            .dueDate(dto.dueDate().withZoneSameInstant(ZONE_ID))
            .nickname(dto.nickname())
            .address(dto.address())
            .isOpened(dto.isOpened())
            .createdDate(dto.createdAt())
            .title(dto.title())
            .build();
    }
}
