package site.timecapsulearchive.core.infra.notification.data.dto;

import java.util.List;

public record FriendRequestNotificationDto(
    String nickname,
    String profileUrl,
    List<Long> friendIds
) {

    public static FriendRequestNotificationDto createOf(
        String nickname,
        String profileUrl,
        List<Long> friendIds
    ) {
        return new FriendRequestNotificationDto(nickname, profileUrl, friendIds);
    }
}
