package site.timecapsulearchive.core.infra.notification.data.dto.request;

import lombok.Builder;

@Builder
public record FriendAcceptNotificationRequest(
    Long memberId,
    String status,
    String title,
    String text

) {

}
