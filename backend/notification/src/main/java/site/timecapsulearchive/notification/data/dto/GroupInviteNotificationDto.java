package site.timecapsulearchive.notification.data.dto;

import java.util.List;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.entity.NotificationStatus;

public record GroupInviteNotificationDto (
    NotificationStatus notificationStatus,
    String groupProfileUrl,
    String title,
    String text,
    List<Long> targetIds
){
    public List<Notification> toNotification(NotificationCategory notificationCategory) {
        return targetIds.stream()
            .map(id -> Notification.builder()
                .notificationCategory(notificationCategory)
                .memberId(id)
                .status(notificationStatus)
                .imageUrl(groupProfileUrl)
                .title(title)
                .text(text)
                .build()
            )
            .toList();
    }
}
