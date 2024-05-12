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
import site.timecapsulearchive.notification.infra.fcm.FCMManager;
import site.timecapsulearchive.notification.repository.member.MemberRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationRepository;

@Service
@RequiredArgsConstructor
public class CapsuleSkinAlarmService implements CapsuleSkinAlarmListener {

    private final FCMManager fcmManager;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;

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

        final String fcmToken = memberRepository.findFCMToken(dto.memberId());
        fcmManager.sendCapsuleSkinNotification(dto, CategoryName.CAPSULE_SKIN, fcmToken);
    }


}
