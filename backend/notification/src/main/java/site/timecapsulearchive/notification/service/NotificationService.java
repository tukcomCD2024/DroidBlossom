package site.timecapsulearchive.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.data.mapper.NotificationMapper;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.infra.fcm.FCMManager;
import site.timecapsulearchive.notification.repository.MemberRepository;
import site.timecapsulearchive.notification.repository.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.NotificationQueryRepository;
import site.timecapsulearchive.notification.repository.NotificationRepository;

@Component
@RequiredArgsConstructor
public class NotificationService implements NotificationServiceListener {

    private final FCMManager fcmManager;
    private final NotificationRepository notificationRepository;
    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final NotificationMapper notificationMapper;
    private final TransactionTemplate transactionTemplate;

    public void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.CAPSULE_SKIN);

                final Notification notification = notificationMapper.capsuleSkinNotificationSendDtoToEntity(
                    dto, notificationCategory);

                notificationRepository.save(notification);
            }
        });

        final String fcmToken = memberRepository.findFCMToken(dto.memberId());
        fcmManager.sendCapsuleSkinNotification(dto, CategoryName.CAPSULE_SKIN, fcmToken);
    }

    public void sendFriendRequestNotification(final FriendNotificationDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.FRIEND_REQUEST);

                final Notification notification = notificationMapper.friendNotificationDtoToEntity(
                    dto, notificationCategory);

                notificationRepository.save(notification);
            }
        });

        final String fcmToken = memberRepository.findFCMToken(dto.targetId());
        fcmManager.sendFriendNotification(dto, CategoryName.FRIEND_REQUEST, fcmToken);
    }



    public void sendFriendRequestNotifications(final FriendNotificationsDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.FRIEND_ACCEPT);

                final List<Notification> notifications = dto.toNotification(notificationCategory);
                notificationQueryRepository.bulkSave(notifications);
            }
        });

        final List<String> fcmTokens = getTargetFcmTokens(dto.targetIds());
        fcmManager.sendFriendNotifications(dto, CategoryName.FRIEND_ACCEPT, fcmTokens);
    }

    private List<String> getTargetFcmTokens(List<Long> targetIds) {
        return memberRepository.findFCMTokens(targetIds)
            .stream()
            .filter(fcmToken -> fcmToken != null && !fcmToken.isBlank())
            .toList();
    }

    public void sendGroupAcceptNotification(final GroupInviteNotificationDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.GROUP_INVITE);
                List<Notification> notifications = dto.toNotification(notificationCategory);
                notificationQueryRepository.bulkSave(notifications);
            }
        });

        List<String> fcmTokens = getTargetFcmTokens(dto.targetIds());
        fcmManager.sendGroupInviteNotifications(dto, CategoryName.GROUP_INVITE, fcmTokens);
    }
}