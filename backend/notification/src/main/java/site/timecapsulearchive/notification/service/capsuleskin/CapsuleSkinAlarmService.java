package site.timecapsulearchive.notification.service.capsuleskin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.global.log.Trace;
import site.timecapsulearchive.notification.infra.fcm.capsuleskin.CapsuleSkinFcmManager;
import site.timecapsulearchive.notification.repository.member.MemberRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationRepository;
import site.timecapsulearchive.notification.service.dto.MemberNotificationInfoDto;
import site.timecapsulearchive.notification.service.validator.NotificationSendValidator;

@Service
@RequiredArgsConstructor
public class CapsuleSkinAlarmService implements CapsuleSkinAlarmListener {

    private final CapsuleSkinFcmManager capsuleSkinFcmManager;
    private final NotificationSendValidator notificationSendValidator;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;

    @Trace
    public void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.CAPSULE_SKIN);

                final Notification notification = dto.toNotification(notificationCategory);

                notificationRepository.save(notification);
            }
        });

        final MemberNotificationInfoDto notificationInfoDto = memberRepository.findFCMToken(
            dto.memberId());
        if (notificationSendValidator.canSend(notificationInfoDto)) {
            capsuleSkinFcmManager.sendCapsuleSkinNotification(dto, CategoryName.CAPSULE_SKIN,
                notificationInfoDto.fcmToken());
        }
    }
}
