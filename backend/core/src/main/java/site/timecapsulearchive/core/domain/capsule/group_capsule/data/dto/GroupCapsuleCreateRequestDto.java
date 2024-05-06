package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;

@Builder
public record GroupCapsuleCreateRequestDto(
    List<Long> groupMemberIds,
    List<String> imageNames,
    List<String> videoNames,
    Long capsuleSkinId,
    String title,
    String content,
    Double longitude,
    Double latitude,
    AddressData addressData,
    ZonedDateTime dueDate
) {

    public Capsule toGroupCapsule(
        Member member,
        CapsuleSkin capsuleSkin,
        Group group,
        CapsuleType capsuleType,
        Point point
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
            .group(group)
            .build();
    }
}
