package site.timecapsulearchive.core.infra.queue.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.notification.entity.NotificationStatus;

@Builder
public record GroupAcceptNotificationDto(
    Long targetId,
    NotificationStatus notificationStatus,
    String title,
    String text
) {

    public static GroupAcceptNotificationDto createOf(
        final Long targetId,
        final String groupMemberNickname
    ) {
        final NotificationRequestMessage groupAcceptRequest = NotificationRequestMessage.GROUP_ACCEPT;

        return new GroupAcceptNotificationDto(
            targetId,
            groupAcceptRequest.getStatus(),
            groupAcceptRequest.getTitle(),
            groupAcceptRequest.buildPrefixText(groupMemberNickname)
        );
    }
}
