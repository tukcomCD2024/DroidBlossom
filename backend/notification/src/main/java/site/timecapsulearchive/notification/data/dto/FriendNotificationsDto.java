package site.timecapsulearchive.notification.data.dto;

import java.util.List;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.entity.NotificationStatus;

public record FriendNotificationsDto(
    String profileUrl,
    NotificationStatus notificationStatus,
    String title,
    String text,
    List<Long> targetIds
) {

    public List<Notification> toNotification(NotificationCategory notificationCategory) {
        return targetIds.stream()
            .map(id -> Notification.builder()
                .memberId(id)
                .notificationCategory(notificationCategory)
                .status(notificationStatus)
                .imageUrl(profileUrl)
                .title(title)
                .text(text)
                .build()
            )
            .toList();
    }
}
