package site.timecapsulearchive.core.domain.capsuleskin.service;


import java.time.ZonedDateTime;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.data.mapper.CapsuleSkinMapper;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinStatusResponse;
import site.timecapsulearchive.core.domain.capsuleskin.data.response.CapsuleSkinsSliceResponse;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.notification.manager.NotificationManager;

@Service
public class CapsuleSkinService {

    private final CapsuleSkinRepository capsuleSkinRepository;
    private final CapsuleSkinQueryRepository capsuleSkinQueryRepository;
    private final CapsuleSkinMessageManager capsuleSkinMessageManager;
    private final MemberRepository memberRepository;
    private final NotificationManager notificationManager;
    private final CapsuleSkinMapper capsuleSkinMapper;
    private final TransactionTemplate transactionTemplate;

    public CapsuleSkinService(
        CapsuleSkinRepository capsuleSkinRepository,
        CapsuleSkinQueryRepository capsuleSkinQueryRepository,
        CapsuleSkinMessageManager capsuleSkinMessageManager,
        MemberRepository memberRepository,
        NotificationManager notificationManager,
        CapsuleSkinMapper capsuleSkinMapper,
        PlatformTransactionManager transactionManager
    ) {
        this.capsuleSkinRepository = capsuleSkinRepository;
        this.capsuleSkinQueryRepository = capsuleSkinQueryRepository;
        this.capsuleSkinMessageManager = capsuleSkinMessageManager;
        this.memberRepository = memberRepository;
        this.notificationManager = notificationManager;
        this.capsuleSkinMapper = capsuleSkinMapper;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setTimeout(7);
    }

    @Transactional(readOnly = true)
    public CapsuleSkinsSliceResponse findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final Slice<CapsuleSkinSummaryDto> slice = capsuleSkinQueryRepository.findCapsuleSkinSliceByCreatedAtAndMemberId(
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

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    capsuleSkinRepository.save(capsuleSkin);
                }
            });

            notificationManager.sendCreatedSkinMessage(memberId, dto);
            return CapsuleSkinStatusResponse.success();
        }

        capsuleSkinMessageManager.sendSkinCreateMessage(memberId, foundMember.getNickname(), dto);
        return CapsuleSkinStatusResponse.sendMessage();
    }

    private boolean isNotExistMotionNameAndRetarget(CapsuleSkinCreateDto dto) {
        return dto.motionName() == null && dto.retarget() == null;
    }
}
