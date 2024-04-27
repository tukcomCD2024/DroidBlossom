package site.timecapsulearchive.core.infra.queue.data.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

public record FriendsReqNotificationDto(
    String nickname,
    String profileUrl,
    NotificationStatus notificationStatus,
    String title,
    String text,
    List<Long> friendIds
) {

    public static FriendsReqNotificationDto createOf(
        final String nickname,
        final String profileUrl,
        final List<Long> friendIds
    ) {
        NotificationRequestMessage friendAcceptRequest = NotificationRequestMessage.FRIEND_REQUEST;

        return new FriendsReqNotificationDto(
            nickname,
            profileUrl,
            friendAcceptRequest.getStatus(),
            friendAcceptRequest.getTitle(),
            friendAcceptRequest.buildPrefixText(nickname),
            friendIds
        );
    }
}
