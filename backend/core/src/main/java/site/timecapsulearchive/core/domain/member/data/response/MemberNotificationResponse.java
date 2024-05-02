package site.timecapsulearchive.core.domain.member.data.response;

import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.CategoryName;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

@Builder
public record MemberNotificationResponse(
    String title,
    String text,
    ZonedDateTime createdAt,
    String imageUrl,
    CategoryName categoryName,
    NotificationStatus status
) {

}
