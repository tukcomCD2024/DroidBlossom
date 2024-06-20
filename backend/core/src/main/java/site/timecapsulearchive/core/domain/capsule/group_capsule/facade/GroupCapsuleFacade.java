package site.timecapsulearchive.core.domain.capsule.group_capsule.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.ImageService;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.VideoService;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleOpenService;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleService;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.service.CapsuleSkinService;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.service.query.GroupQueryService;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.domain.member_group.service.MemberGroupQueryService;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

@Component
@RequiredArgsConstructor
public class GroupCapsuleFacade {

    private final MemberService memberService;
    private final GroupCapsuleService groupCapsuleService;
    private final GroupQueryService groupQueryService;
    private final MemberGroupQueryService memberGroupQueryService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final GeoTransformManager geoTransformManager;
    private final CapsuleSkinService capsuleSkinService;
    private final GroupCapsuleOpenService groupCapsuleOpenService;

    @Transactional
    public void saveGroupCapsule(
        final GroupCapsuleCreateRequestDto dto,
        final Long memberId,
        final Long groupId
    ) {
        final Member member = memberService.findMemberById(memberId);
        final Group group = groupQueryService.findGroupById(groupId);
        final CapsuleSkin capsuleSkin = capsuleSkinService.findCapsuleSkinById(dto.capsuleSkinId());

        final Point point = geoTransformManager.changePoint4326To3857(dto.latitude(),
            dto.longitude());

        final Capsule capsule = groupCapsuleService.saveGroupCapsule(dto, member, group,
            capsuleSkin,
            point);

        imageService.bulkSave(dto.imageNames(), capsule, member);
        videoService.bulkSave(dto.videoNames(), capsule, member);

        final List<Long> groupMemberIds = memberGroupQueryService.findGroupMemberIds(groupId);

        groupCapsuleOpenService.bulkSave(groupId, groupMemberIds, capsule);
    }
}