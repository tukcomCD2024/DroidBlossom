package site.timecapsulearchive.core.domain.member.data.dto;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.member.entity.CategoryName;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

public record MemberNotificationDto(
    String title,
    String text,
    ZonedDateTime createdAt,
    String imageUrl,
    CategoryName categoryName,
    NotificationStatus status
) {

}
