package site.timecapsulearchive.core.domain.member.data.response;

import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record MemberNotificationResponse(
    String title,
    String text,
    ZonedDateTime createdAt,
    String imageUrl
) {

}
