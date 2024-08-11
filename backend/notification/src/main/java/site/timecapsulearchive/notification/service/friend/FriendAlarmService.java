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
import site.timecapsulearchive.notification.global.log.Trace;
import site.timecapsulearchive.notification.infra.fcm.friend.FriendFcmManager;
import site.timecapsulearchive.notification.repository.member.MemberRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationRepository;
import site.timecapsulearchive.notification.service.dto.MemberNotificationInfoDto;
import site.timecapsulearchive.notification.service.validator.NotificationSendValidator;

@Service
@RequiredArgsConstructor
public class FriendAlarmService implements FriendAlarmListener {

    private final FriendFcmManager friendFcmManager;
    private final NotificationSendValidator notificationSendValidator;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;

    @Trace
    @Override
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

        final MemberNotificationInfoDto notificationInfoDto = memberRepository.findFCMToken(
            dto.targetId());
        if (notificationSendValidator.canSend(notificationInfoDto)) {
            friendFcmManager.sendFriendNotification(dto, CategoryName.FRIEND_REQUEST,
                notificationInfoDto.fcmToken());
        }
    }

    @Trace
    @Override
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

        final MemberNotificationInfoDto notificationInfoDto = memberRepository.findFCMToken(
            dto.targetId());
        if (notificationSendValidator.canSend(notificationInfoDto)) {
            friendFcmManager.sendFriendNotification(dto, CategoryName.FRIEND_ACCEPT,
                notificationInfoDto.fcmToken());
        }
    }

    @Trace
    @Override
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

        final List<String> filteredFCMTokens = memberRepository.findFCMTokens(dto.targetIds())
            .stream()
            .filter(notificationSendValidator::canSend)
            .map(MemberNotificationInfoDto::fcmToken)
            .toList();

        if (!filteredFCMTokens.isEmpty()) {
            friendFcmManager.sendFriendNotifications(dto, CategoryName.FRIEND_ACCEPT,
                filteredFCMTokens);
        }
    }
}
