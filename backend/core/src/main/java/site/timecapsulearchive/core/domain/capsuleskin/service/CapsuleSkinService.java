package site.timecapsulearchive.core.domain.capsuleskin.service;


import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinDeleteResultDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.mapper.CapsuleSkinMapper;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinStatusResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.exception.CapsuleSkinNotFoundException;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.queue.manager.CapsuleSkinMessageManager;

@Service
@RequiredArgsConstructor
public class CapsuleSkinService {

    private final CapsuleSkinRepository capsuleSkinRepository;
    private final CapsuleSkinMessageManager capsuleSkinMessageManager;
    private final MemberRepository memberRepository;
    private final CapsuleSkinMapper capsuleSkinMapper;
    private final TransactionTemplate transactionTemplate;

    @Transactional(readOnly = true)
    public CapsuleSkinsSliceResponse findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<CapsuleSkinSummaryDto> slice = capsuleSkinRepository.findCapsuleSkinSliceByCreatedAtAndMemberId(
            memberId,
            size,
            createdAt
        );

        return capsuleSkinMapper.capsuleSkinSliceToResponse(slice.getContent(), slice.hasNext());
    }

    public CapsuleSkinStatusResponse sendCapsuleSkinCreateMessage(
        final Long memberId,
        final CapsuleSkinCreateDto dto
    ) {
        Member foundMember = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        if (isNotExistMotionNameAndRetarget(dto)) {
            CapsuleSkin capsuleSkin = capsuleSkinMapper.createDtoToEntity(dto, foundMember);

            transactionTemplate.executeWithoutResult(status ->
                capsuleSkinRepository.save(capsuleSkin)
            );
            return CapsuleSkinStatusResponse.success();
        }

        capsuleSkinMessageManager.sendSkinCreateMessage(memberId, foundMember.getNickname(), dto);
        return CapsuleSkinStatusResponse.sendMessage();
    }

    private boolean isNotExistMotionNameAndRetarget(final CapsuleSkinCreateDto dto) {
        return dto.motionName() == null && dto.retarget() == null;
    }

    @Transactional(readOnly = true)
    public CapsuleSkin findCapsuleSkinById(final Long capsuleSkinId) {
        return capsuleSkinRepository.findCapsuleSkinById(capsuleSkinId)
            .orElseThrow(CapsuleSkinNotFoundException::new);
    }

    @Transactional
    public CapsuleSkinDeleteResultDto deleteCapsuleSkin(
        final Long memberId,
        final Long capsuleSkinId
    ) {
        boolean existRelatedCapsule = capsuleSkinRepository.existRelatedCapsule(memberId, capsuleSkinId);
        if (existRelatedCapsule) {
            return CapsuleSkinDeleteResultDto.fail();
        }

        CapsuleSkin capsuleSkin = capsuleSkinRepository.findCapsuleSkinByMemberIdAndId(memberId, capsuleSkinId)
            .orElseThrow(CapsuleSkinNotFoundException::new);
        capsuleSkinRepository.delete(capsuleSkin);

        return CapsuleSkinDeleteResultDto.success();
    }
}
