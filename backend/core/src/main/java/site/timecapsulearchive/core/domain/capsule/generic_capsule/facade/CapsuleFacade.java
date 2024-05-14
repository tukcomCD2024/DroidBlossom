package site.timecapsulearchive.core.domain.capsule.generic_capsule.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleCreateRequestDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleOpenedResponse;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.CapsuleService;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.ImageService;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.service.VideoService;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.service.CapsuleSkinService;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;

@Component
@RequiredArgsConstructor
public class CapsuleFacade {

    private final CapsuleService capsuleService;
    private final MemberService memberService;
    private final CapsuleSkinService capsuleSkinService;
    private final ImageService imageService;
    private final VideoService videoService;

    @Transactional
    public CapsuleOpenedResponse updateCapsuleOpened(final Long memberId, final Long capsuleId) {
        final Capsule findCapsule = capsuleService.findCapsuleByMemberIdAndCapsuleId(memberId,
            capsuleId);

        if (findCapsule.isNotCapsuleOpened()) {
            return CapsuleOpenedResponse.notOpened();
        }

        capsuleService.updateIsOpenedTrue(memberId, capsuleId);
        return CapsuleOpenedResponse.opened();
    }

    /**
     * 멤버 아이디와 캡슐 생성 포맷을 받아서 캡슐을 생성한다.
     *
     * @param memberId 캡슐을 생성할 멤버 아이디
     * @param dto      캡슐 생성 요청 포맷
     */
    @Transactional
    public void saveCapsule(
        final Long memberId,
        final CapsuleCreateRequestDto dto,
        final CapsuleType capsuleType
    ) {
        final Member member = memberService.findMemberById(memberId);
        final CapsuleSkin capsuleSkin = capsuleSkinService.findCapsuleSkinById(
            dto.capsuleSkinId());

        final Capsule capsule = capsuleService.saveCapsule(dto, member, capsuleSkin, capsuleType);

        imageService.bulkSave(dto.imageNames(), capsule, member);
        videoService.bulkSave(dto.videoNames(), capsule, member);
    }

}
