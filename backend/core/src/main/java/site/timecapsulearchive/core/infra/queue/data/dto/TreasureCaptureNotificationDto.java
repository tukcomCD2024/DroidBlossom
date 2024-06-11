package site.timecapsulearchive.core.infra.queue.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

@Builder
public record TreasureCaptureNotificationDto(

    Long targetId,
    String treasureImageUrl,
    NotificationStatus notificationStatus,
    String title,
    String text
) {

    public static TreasureCaptureNotificationDto createOf(
        final Long targetId,
        final String treasureImageUrl,
        final String memberNickname

    ) {
        final NotificationRequestMessage treasureCaptureRequest = NotificationRequestMessage.TREASURE_CAPTURE;

        return new TreasureCaptureNotificationDto(
            targetId,
            treasureImageUrl,
            treasureCaptureRequest.getStatus(),
            treasureCaptureRequest.getTitle(),
            treasureCaptureRequest.buildPrefixText(memberNickname)
        );
    }
}
