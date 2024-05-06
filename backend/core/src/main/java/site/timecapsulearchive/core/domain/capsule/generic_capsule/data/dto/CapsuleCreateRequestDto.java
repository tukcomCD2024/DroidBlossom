package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;

@Builder
public record CapsuleCreateRequestDto(
    Long capsuleSkinId,
    String title,
    String content,
    Double longitude,
    Double latitude,
    AddressData addressData,
    ZonedDateTime dueDate,
    List<String> imageNames,
    List<String> videoNames,
    String directory
) {

    public Capsule toCapsule(
        final Point point,
        final Member member,
        final CapsuleSkin capsuleSkin,
        final CapsuleType capsuleType
    ) {
        return Capsule.builder()
            .title(title)
            .content(content)
            .point(point)
            .address(addressData.toAddress())
            .type(capsuleType)
            .member(member)
            .dueDate(dueDate)
            .capsuleSkin(capsuleSkin)
            .build();
    }
}
