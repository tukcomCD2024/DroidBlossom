package site.timecapsulearchive.core.infra.notification.data.dto.request;

import lombok.Builder;

@Builder
public record CreatedCapsuleSkinNotificationRequest(
    Long memberId,
    String status,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

}
