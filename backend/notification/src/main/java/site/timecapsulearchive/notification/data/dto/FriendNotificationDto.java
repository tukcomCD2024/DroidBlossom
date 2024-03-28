package site.timecapsulearchive.notification.data.dto;

import java.util.Objects;
import lombok.Builder;
import site.timecapsulearchive.notification.entity.NotificationStatus;

@Builder
public record FriendNotificationDto(
    Long memberId,
    NotificationStatus status,
    String text,
    String title
) {

    public FriendNotificationDto {
        Objects.requireNonNull(memberId);
        Objects.requireNonNull(status);
        Objects.requireNonNull(text);
        Objects.requireNonNull(title);
    }
}
