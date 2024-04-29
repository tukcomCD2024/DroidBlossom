package site.timecapsulearchive.core.infra.queue.data.request;

import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;
import site.timecapsulearchive.core.infra.queue.data.dto.NotificationRequestMessage;

@Builder
public record FriendReqNotificationDto(
    Long targetId,
    NotificationStatus notificationStatus,
    String title,
    String text
) {

    public static FriendReqNotificationDto createOf(
        final Long friendId,
        final String ownerNickname
    ) {
        final NotificationRequestMessage friendReqRequest = NotificationRequestMessage.FRIEND_REQUEST;

        return new FriendReqNotificationDto(
            friendId,
            friendReqRequest.getStatus(),
            friendReqRequest.getTitle(),
            friendReqRequest.buildPrefixText(ownerNickname)
        );
    }
}
