package site.timecapsulearchive.core.infra.queue.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

@Builder
public record TreasureCaptureNotificationDto(

    Long targetId,
    String treasureUrl,
    NotificationStatus notificationStatus,
    String title,
    String text
) {

    public static TreasureCaptureNotificationDto createOf(
        final Long targetId,
        final String treasureUrl,
        final String memberNickname

    ) {
        final NotificationRequestMessage friendAcceptRequest = NotificationRequestMessage.TREASURE_CAPTURE;

        return new TreasureCaptureNotificationDto(
            targetId,
            treasureUrl,
            friendAcceptRequest.getStatus(),
            friendAcceptRequest.getTitle(),
            friendAcceptRequest.buildPrefixText(memberNickname)
        );
    }
}
