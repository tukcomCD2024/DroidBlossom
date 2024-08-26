package site.timecapsulearchive.core.infra.queue.data.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.notification.entity.NotificationStatus;

public record FriendsReqNotificationsDto(
    String profileUrl,
    NotificationStatus notificationStatus,
    String title,
    String text,
    List<Long> targetIds
) {

    public static FriendsReqNotificationsDto createOf(
        final String nickname,
        final String profileUrl,
        final List<Long> targetIds
    ) {
        NotificationRequestMessage friendAcceptRequest = NotificationRequestMessage.FRIEND_REQUEST;

        return new FriendsReqNotificationsDto(
            profileUrl,
            friendAcceptRequest.getStatus(),
            friendAcceptRequest.getTitle(),
            friendAcceptRequest.buildPrefixText(nickname),
            targetIds
        );
    }
}
