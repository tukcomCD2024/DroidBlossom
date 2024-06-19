package site.timecapsulearchive.core.domain.friend.data.dto;

import java.util.List;

public record FriendInviteNotificationDto(
    String nickname,
    String profileUrl,
    List<Long> foundFriendIds
) {

    public static FriendInviteNotificationDto create(
        final String nickname,
        final String profileUrl,
        final List<Long> foundFriendIds
    ) {
        return new FriendInviteNotificationDto(nickname, profileUrl, foundFriendIds);
    }
}
