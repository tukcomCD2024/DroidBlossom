package site.timecapsulearchive.core.domain.member.data.dto;

import java.time.ZonedDateTime;
import java.util.function.UnaryOperator;
import site.timecapsulearchive.core.domain.member.data.response.MemberNotificationResponse;
import site.timecapsulearchive.core.domain.member.entity.CategoryName;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

public record MemberNotificationDto(
    String title,
    String text,
    ZonedDateTime createdAt,
    String imageUrl,
    CategoryName categoryName,
    NotificationStatus status
) {

    public MemberNotificationDto {
        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }

    public MemberNotificationResponse toResponse(UnaryOperator<String> getS3PreSignedUrlForGet) {
        String notificationImageUrl = "";
        if (imageUrl != null) {
            notificationImageUrl = getS3PreSignedUrlForGet.apply(imageUrl);
        }

        return MemberNotificationResponse.builder()
            .title(title)
            .text(text)
            .createdAt(createdAt)
            .imageUrl(notificationImageUrl)
            .categoryName(categoryName)
            .status(status)
            .build();
    }

}
