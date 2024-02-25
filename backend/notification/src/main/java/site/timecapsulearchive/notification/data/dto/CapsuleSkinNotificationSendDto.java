package site.timecapsulearchive.notification.data.dto;

import lombok.Builder;

@Builder
public record CapsuleSkinNotificationSendDto(

    Long memberId,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

}
