package site.timecapsulearchive.notification.service.group;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.notification.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.global.aop.Trace;
import site.timecapsulearchive.notification.infra.fcm.group.GroupFcmManager;
import site.timecapsulearchive.notification.repository.member.MemberRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.notification.NotificationRepository;
import site.timecapsulearchive.notification.service.dto.MemberNotificationInfoDto;
import site.timecapsulearchive.notification.service.validator.NotificationSendValidator;

@Service
@RequiredArgsConstructor
public class GroupAlarmService implements GroupAlarmListener {

    private final GroupFcmManager groupFcmManager;
    private final NotificationSendValidator notificationSendValidator;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final TransactionTemplate transactionTemplate;

    @Trace
    @Override
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

        List<String> filteredFCMTokens = memberRepository.findFCMTokens(
                dto.targetIds())
            .stream()
            .filter(notificationSendValidator::canSend)
            .map(MemberNotificationInfoDto::fcmToken)
            .toList();

        if (!filteredFCMTokens.isEmpty()) {
            groupFcmManager.sendGroupInviteNotifications(dto, CategoryName.GROUP_INVITE,
                filteredFCMTokens);
        }
    }

    @Trace
    @Override
    public void sendGroupAcceptNotification(GroupAcceptNotificationDto dto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
                    CategoryName.GROUP_ACCEPT);

                final Notification notification = dto.toNotification(notificationCategory);

                notificationRepository.save(notification);

            }
        });

        final MemberNotificationInfoDto notificationInfoDto = memberRepository.findFCMToken(
            dto.targetId());
        if (notificationSendValidator.canSend(notificationInfoDto)) {
            groupFcmManager.sendGroupAcceptNotification(dto, CategoryName.FRIEND_ACCEPT,
                notificationInfoDto.fcmToken());
        }
    }
}
