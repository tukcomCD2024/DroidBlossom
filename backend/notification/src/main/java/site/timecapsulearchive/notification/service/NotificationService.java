package site.timecapsulearchive.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
public class NotificationService {

    private final FCMManager fcmManager;
    private final NotificationRepository notificationRepository;
    private final NotificationQueryRepository notificationQueryRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final NotificationMapper notificationMapper;
    private final TransactionTemplate transactionTemplate;

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "capsuleSkin.queue", durable = "true"),
            exchange = @Exchange(value = "capsuleSkin.exchange"),
            key = "capsuleSkin.queue"
        ),
        returnExceptions = "false"
    )
    public void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto) {
        final CategoryName categoryName = CategoryName.CAPSULE_SKIN;

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    categoryName);

                final Notification notification = notificationMapper.capsuleSkinNotificationSendDtoToEntity(
                    dto, notificationCategory);

                notificationRepository.save(notification);
            }
        });

        final String fcmToken = memberRepository.findFCMToken(dto.memberId());
        if (fcmToken != null && !fcmToken.isBlank()) {
            fcmManager.sendCapsuleSkinNotification(dto, categoryName, fcmToken);
        }
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "friendRequest.queue", durable = "true"),
            exchange = @Exchange(value = "friendRequest.exchange"),
            key = "friendRequest.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    public void sendFriendRequestsNotification(final FriendNotificationDto dto) {
        final CategoryName categoryName = CategoryName.FRIEND_REQUEST;

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                saveFriendNotification(categoryName, dto);
            }
        });

        sendFCM(dto, categoryName);
    }

    private void saveFriendNotification(
        final CategoryName categoryName,
        final FriendNotificationDto dto
    ) {
        final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
            categoryName);

        final Notification notification = notificationMapper.friendNotificationDtoToEntity(dto,
            notificationCategory);

        notificationRepository.save(notification);
    }

    private void sendFCM(FriendNotificationDto dto, CategoryName categoryName) {
        final String fcmToken = memberRepository.findFCMToken(dto.targetId());
        if (fcmToken != null && !fcmToken.isBlank()) {
            fcmManager.sendFriendNotification(dto, categoryName, fcmToken);
        }
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "groupInvite.queue", durable = "true"),
            exchange = @Exchange(value = "groupInvite.exchange"),
            key = "groupInvite.queue"
        ),
        returnExceptions = "false"
    )
    public void sendFriendAcceptNotification(final GroupInviteNotificationDto dto) {
        if (dto.targetIds().isEmpty()) {
            return;
        }

        final CategoryName categoryName = CategoryName.GROUP_INVITE;
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    categoryName);
                List<Notification> notifications = dto.toNotification(notificationCategory);
                notificationQueryRepository.bulkSave(notifications);
            }
        });

        List<String> fcmTokens = memberRepository.findFCMTokens(dto.targetIds())
            .stream()
            .filter(fcmToken -> fcmToken != null && !fcmToken.isBlank())
            .toList();

        if (fcmTokens.isEmpty()) {
            return;
        }

        fcmManager.sendGroupInviteNotifications(dto, categoryName, fcmTokens);
    }

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "friendRequests.queue", durable = "true"),
            exchange = @Exchange(value = "friendRequests.exchange"),
            key = "friendRequests.queue"
        ),
        returnExceptions = "false",
        messageConverter = "jsonMessageConverter"
    )
    public void sendFriendRequestNotifications(final FriendNotificationsDto dto) {
        if (dto.targetIds().isEmpty()) {
            return;
        }

        final CategoryName categoryName = CategoryName.FRIEND_ACCEPT;
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    categoryName);

                final List<Notification> notifications = dto.toNotification(notificationCategory);
                notificationQueryRepository.bulkSave(notifications);
            }
        });


        final List<String> fcmTokens = memberRepository.findFCMTokens(dto.targetIds())
            .stream()
            .filter(fcmToken -> fcmToken != null && !fcmToken.isBlank())
            .toList();

        if (fcmTokens.isEmpty()) {
            return;
        }

        fcmManager.sendFriendNotifications(dto, categoryName, fcmTokens);
    }
}