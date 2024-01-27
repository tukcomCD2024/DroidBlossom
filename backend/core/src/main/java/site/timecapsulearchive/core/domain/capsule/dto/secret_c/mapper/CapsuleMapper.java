package site.timecapsulearchive.core.domain.capsule.dto.secret_c.mapper;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.response.CapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust.SecretCapsuleCreateRequest;
import site.timecapsulearchive.core.domain.capsule.entity.Address;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Component
public class CapsuleMapper {

    public CapsuleCreateRequestDto secretCapsuleCreateRequestToDto(
        SecretCapsuleCreateRequest request) {
        return new CapsuleCreateRequestDto(
            request.capsuleSkinId(),
            request.title(),
            request.content(),
            request.longitude(),
            request.latitude(),
            request.dueDate(),
            request.fileNames(),
            request.directory(),
            request.capsuleType()
        );
    }

    public Capsule requestDtoToEntity(
        CapsuleCreateRequestDto dto,
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

    public CapsuleSummaryResponse capsuleSummaryResponseToDto(CapsuleSummaryDto dto) {
        Point point = geoTransformer.changePoint3857To4326(dto.point());

        return CapsuleSummaryResponse.builder()
            .id(dto.id())
            .longitude(point.getX())
            .latitude(point.getY())
            .nickname(dto.nickname())
            .capsuleSkinUrl(dto.skinUrl())
            .title(dto.title())
            .dueDate(dto.dueDate())
            .build();
    }
}
