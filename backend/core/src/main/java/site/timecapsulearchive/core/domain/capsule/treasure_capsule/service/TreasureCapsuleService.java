package site.timecapsulearchive.core.domain.capsule.treasure_capsule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsule.entity.Image;
import site.timecapsulearchive.core.domain.capsule.exception.CapsuleNotFondException;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image.ImageRepository;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.exception.TreasureCapsuleOpenFailedException;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.repository.TreasureCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;
import site.timecapsulearchive.core.domain.capsuleskin.repository.CapsuleSkinRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Service
@RequiredArgsConstructor
public class TreasureCapsuleService {

    private final TreasureCapsuleQueryRepository treasureCapsuleQueryRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final CapsuleSkinRepository capsuleSkinRepository;
    private final SocialNotificationManager socialNotificationManager;
    private final TransactionTemplate transactionTemplate;

    public void openTreasureCapsule(final Long memberId, final Long capsuleId, final Long imageId) {
        final Member member = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        String treasureImageUrl = transactionTemplate.execute(status -> {
            // imageId로 image 엔티티의 imageUrl을 가져온 후 삭제
            final String imageUrl = imageRepository.findImageUrl(imageId)
                .orElseThrow(CapsuleNotFondException::new);
            imageRepository.deleteImage(imageId);

            // 보물 캡슐 삭제
            final Long deleteTreasureCapsule = treasureCapsuleQueryRepository.deleteTreasureCapsule(
                capsuleId).orElseThrow(
                CapsuleNotFondException::new);
            if (deleteTreasureCapsule != 1) {
                throw new TreasureCapsuleOpenFailedException();
            }

            // caspuleSkin 저장
            final CapsuleSkin myTreasureCapsuleSkin = CapsuleSkin.captureTreasureCapsuleSkin(
                imageUrl, member);
            capsuleSkinRepository.save(myTreasureCapsuleSkin);

            return imageUrl;
        });

        // 알림 전송
        socialNotificationManager.sendTreasureCaptureMessage(memberId, member.getNickname(),
            treasureImageUrl);
    }
}
