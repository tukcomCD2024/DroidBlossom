package site.timecapsulearchive.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.mapper.NotificationMapper;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.infra.fcm.FCMManager;
import site.timecapsulearchive.notification.repository.MemberRepository;
import site.timecapsulearchive.notification.repository.NotificationCategoryRepository;
import site.timecapsulearchive.notification.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FCMManager fcmManager;
    private final NotificationRepository notificationRepository;
    private final NotificationCategoryRepository notificationCategoryRepository;
    private final MemberRepository memberRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void sendCapsuleSkinAlarm(final CapsuleSkinNotificationSendDto dto) {
        final CategoryName categoryName = CategoryName.CAPSULE_SKIN;
        final NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
            categoryName);

        final Notification notification = notificationMapper.capsuleSkinNotificationSendDtoToEntity(
            dto,
            notificationCategory);

        notificationRepository.save(notification);

        final String fcmToken = memberRepository.findFCMToken(dto.memberId());
        if (!fcmToken.isBlank()) {
            fcmManager.sendCapsuleSkinNotification(dto, categoryName, fcmToken);
        }
    }

    @Transactional
    public void sendFriendRequestsNotification(final FriendNotificationDto dto) {
        final CategoryName categoryName = CategoryName.FRIEND_REQUEST;
        saveFriendNotification(categoryName, dto);

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
        final String fcmToken = memberRepository.findFCMToken(dto.memberId());
        if (!fcmToken.isBlank()) {
            fcmManager.sendFriendNotification(dto, categoryName, fcmToken);
        }
    }

    @Transactional
    public void sendFriendAcceptNotification(final FriendNotificationDto dto) {
        final CategoryName categoryName = CategoryName.FRIEND_ACCEPT;
        saveFriendNotification(categoryName, dto);

        sendFCM(dto, categoryName);
    }
}