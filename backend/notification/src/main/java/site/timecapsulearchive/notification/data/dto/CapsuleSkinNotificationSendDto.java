package site.timecapsulearchive.notification.data.dto;

import lombok.Builder;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.entity.NotificationStatus;

@Builder
public record CapsuleSkinNotificationSendDto(

    Long memberId,
    NotificationStatus status,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

    public Notification toNotification(NotificationCategory notificationCategory) {
        return Notification.builder()
            .memberId(memberId)
            .title(title)
            .text(text)
            .imageUrl(skinUrl)
            .notificationCategory(notificationCategory)
            .status(status)
            .build();
    }
}
