package site.timecapsulearchive.core.domain.capsule.facade;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.service.GroupCapsuleService;
import site.timecapsulearchive.core.domain.capsule.service.ImageService;
import site.timecapsulearchive.core.domain.capsule.service.VideoService;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.service.CapsuleSkinService;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.service.GroupService;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;

@Component
@RequiredArgsConstructor
public class GroupCapsuleFacade {

    private final MemberService memberService;
    private final GroupCapsuleService groupCapsuleService;
    private final GroupService groupService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final GeoTransformManager geoTransformManager;
    private final CapsuleSkinService capsuleSkinService;

    @Transactional
    public void saveGroupCapsule(
        GroupCapsuleCreateRequestDto dto,
        Long memberId,
        Long groupId
    ) {
        Member member = memberService.findMemberById(memberId);
        Group group = groupService.findGroupById(groupId);
        CapsuleSkin capsuleSkin = capsuleSkinService.findCapsuleSkinById(dto.capsuleSkinId());

        Point point = geoTransformManager.changePoint4326To3857(dto.latitude(), dto.longitude());

        Capsule capsule = groupCapsuleService.saveGroupCapsule(dto, member, group, capsuleSkin,
            point);

        imageService.bulkSave(dto.imageNames(), capsule, member);
        videoService.bulkSave(dto.videoNames(), capsule, member);
    }
}