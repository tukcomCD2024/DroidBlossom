package site.timecapsulearchive.core.domain.member.data.dto;

import java.time.ZonedDateTime;

public record MemberNotificationDto(
    String title,
    String text,
    ZonedDateTime createdAt,
    String imageUrl
) {

}
