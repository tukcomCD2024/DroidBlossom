package site.timecapsulearchive.core.domain.capsule.dto.secret_c.mapper;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
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
}
