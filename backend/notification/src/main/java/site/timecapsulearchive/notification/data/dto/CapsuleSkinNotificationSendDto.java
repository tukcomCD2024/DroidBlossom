package site.timecapsulearchive.notification.data.dto;

import lombok.Builder;
import site.timecapsulearchive.notification.entity.NotificationStatus;

@Builder
public record CapsuleSkinNotificationSendDto(

    Long memberId,
    NotificationStatus status,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

}
