package site.timecapsulearchive.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
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
    public void sendCapsuleSkinAlarm(CapsuleSkinNotificationSendDto dto) {
        NotificationCategory notificationCategory = notificationCategoryRepository.findByCategoryName(
            CategoryName.CAPSULE_SKIN);

        Notification notification = notificationMapper.capsuleSkinNotificationSendDtoToEntity(dto,
            notificationCategory);

        notificationRepository.save(notification);

        String fcmToken = memberRepository.findFCMToken(dto.memberId());
        if (!fcmToken.isBlank()) {
            fcmManager.send(dto, notificationCategory.getCategoryName(), fcmToken);
        }
    }
}