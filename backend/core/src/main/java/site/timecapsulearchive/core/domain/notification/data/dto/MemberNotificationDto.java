package site.timecapsulearchive.core.domain.notification.data.dto;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.notification.entity.CategoryName;
import site.timecapsulearchive.core.domain.notification.entity.NotificationStatus;

public record MemberNotificationDto(
    String title,
    String text,
    ZonedDateTime createdAt,
    String imageUrl,
    CategoryName categoryName,
    NotificationStatus status
) {

}
