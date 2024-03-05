package site.timecapsulearchive.notification.data.dto;

import lombok.Builder;
import site.timecapsulearchive.notification.entity.CapsuleSkinCreationStatus;

@Builder
public record CapsuleSkinNotificationSendDto(

    Long memberId,
    CapsuleSkinCreationStatus status,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

}
