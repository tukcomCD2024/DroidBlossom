package site.timecapsulearchive.core.domain.capsule.treasure_capsule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule.CapsuleRepository;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleOpenDto;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class TreasureCapsuleService {

    private final MemberRepository memberRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;
    private final TransactionTemplate transactionTemplate;

    public TreasureCapsuleOpenDto openTreasureCapsule(final Long memberId, final Long capsuleId) {
        final Member member = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        String treasureImageUrl = transactionTemplate.execute(status -> {
            final Capsule treasureCapsule = capsuleRepository.findCapsuleWithImageByCapsuleId(
                capsuleId).orElseThrow(CapsuleNotFondException::new);

            final Image image = treasureCapsule.getImages().get(0);
            final String imageUrl = image.getImageUrl();
            capsuleRepository.delete(treasureCapsule);

            final boolean isAlreadyGetTreasure = capsuleSkinRepository.existByImageUrlAndMemberId(
                imageUrl, member.getId());
            if (isAlreadyGetTreasure) {
                return imageUrl;
            }

            final CapsuleSkin myTreasureCapsuleSkin = CapsuleSkin.captureTreasureCapsuleSkin(
                imageUrl, member);
            capsuleSkinRepository.save(myTreasureCapsuleSkin);

            return imageUrl;
        });

        return new TreasureCapsuleOpenDto(treasureImageUrl);
    }

    @Transactional(readOnly = true)
    public TreasureCapsuleSummaryDto findTreasureCapsuleSummary(
        final Long capsuleId
    ) {
        return capsuleRepository.findTreasureCapsuleSummary(capsuleId)
            .orElseThrow(CapsuleNotFondException::new);
    }
}
