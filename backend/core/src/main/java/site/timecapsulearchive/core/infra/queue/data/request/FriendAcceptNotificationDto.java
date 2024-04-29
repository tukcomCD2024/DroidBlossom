package site.timecapsulearchive.core.infra.queue.data.request;

import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;
import site.timecapsulearchive.core.infra.queue.data.dto.NotificationRequestMessage;

@Builder
public record FriendAcceptNotificationDto(
    Long targetId,
    NotificationStatus notificationStatus,
    String title,
    String text
) {

    public static FriendAcceptNotificationDto createOf(final Long friendId,
        final String ownerNickname) {
        final NotificationRequestMessage friendAcceptRequest = NotificationRequestMessage.FRIEND_ACCEPT;

        return new FriendAcceptNotificationDto(
            friendId,
            friendAcceptRequest.getStatus(),
            friendAcceptRequest.getTitle(),
            friendAcceptRequest.buildPrefixText(ownerNickname)
        );
    }
}
