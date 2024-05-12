package site.timecapsulearchive.notification.service.group;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.infra.fcm.FCMManager;
import site.timecapsulearchive.notification.repository.member.MemberRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationRepository;

@Service
@RequiredArgsConstructor
public class GroupAlarmService implements GroupAlarmListener {

    private final FCMManager fcmManager;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;


    public void sendGroupInviteNotification(final GroupInviteNotificationDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.GROUP_INVITE);
                List<Notification> notifications = dto.toNotification(notificationCategory);
                notificationRepository.bulkSave(notifications);
            }
        });

        List<String> fcmTokens = getTargetFcmTokens(dto.targetIds());
        if (fcmTokens != null && !fcmTokens.isEmpty()) {
            fcmManager.sendGroupInviteNotifications(dto, CategoryName.GROUP_INVITE, fcmTokens);
        }
    }

    private List<String> getTargetFcmTokens(List<Long> targetIds) {
        return memberRepository.findFCMTokens(targetIds)
            .stream()
            .toList();
    }

}
