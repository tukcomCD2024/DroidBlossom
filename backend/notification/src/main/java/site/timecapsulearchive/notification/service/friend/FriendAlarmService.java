package site.timecapsulearchive.notification.service.friend;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.infra.fcm.friend.FriendFcmManager;
import site.timecapsulearchive.notification.repository.member.MemberRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationRepository;

@Service
@RequiredArgsConstructor
public class FriendAlarmService implements FriendAlarmListener {

    private final FriendFcmManager friendFcmManager;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;


    public void sendFriendRequestNotification(final FriendNotificationDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.FRIEND_REQUEST);

                final Notification notification = dto.toNotification(notificationCategory);

                notificationRepository.save(notification);
            }
        });

        final String fcmToken = memberRepository.findFCMToken(dto.targetId());
        if (fcmToken != null && !fcmToken.isBlank()) {
            friendFcmManager.sendFriendNotification(dto, CategoryName.FRIEND_REQUEST, fcmToken);
        }
    }

    public void sendFriendAcceptNotification(final FriendNotificationDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.FRIEND_ACCEPT);

                final Notification notification = dto.toNotification(notificationCategory);

                notificationRepository.save(notification);
            }
        });

        final String fcmToken = memberRepository.findFCMToken(dto.targetId());
        if (fcmToken != null && !fcmToken.isBlank()) {
            friendFcmManager.sendFriendNotification(dto, CategoryName.FRIEND_ACCEPT, fcmToken);
        }
    }


    public void sendFriendRequestNotifications(final FriendNotificationsDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.FRIEND_ACCEPT);

                final List<Notification> notifications = dto.toNotification(notificationCategory);
                notificationRepository.bulkSave(notifications);
            }
        });

        final List<String> fcmTokens = getTargetFcmTokens(dto.targetIds());
        if (fcmTokens != null && !fcmTokens.isEmpty()) {
            friendFcmManager.sendFriendNotifications(dto, CategoryName.FRIEND_ACCEPT, fcmTokens);
        }
    }

    private List<String> getTargetFcmTokens(List<Long> targetIds) {
        return memberRepository.findFCMTokens(targetIds)
            .stream()
            .toList();
    }

}
