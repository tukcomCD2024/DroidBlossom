package site.timecapsulearchive.notification.data.dto;

import java.util.Objects;
import lombok.Builder;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;
import site.timecapsulearchive.notification.entity.NotificationStatus;

@Builder
public record GroupAcceptNotificationDto(
    Long targetId,
    NotificationStatus notificationStatus,
    String text,
    String title
) {

    public GroupAcceptNotificationDto {
        Objects.requireNonNull(targetId);
        Objects.requireNonNull(notificationStatus);
        Objects.requireNonNull(text);
        Objects.requireNonNull(title);
    }

    public Notification toNotification(final NotificationCategory notificationCategory) {
        return Notification.builder()
            .memberId(targetId)
            .title(title)
            .text(text)
            .status(notificationStatus)
            .notificationCategory(notificationCategory)
            .build();
    }
}
