package site.timecapsulearchive.core.domain.friend.data.request;

import java.time.ZonedDateTime;

public record FriendBeforeGroupInviteRequest(
    Long memberId,
    Long groupId,
    int size,
    ZonedDateTime createdAt
) {

    public static FriendBeforeGroupInviteRequest of(
        final Long memberId,
        final Long groupId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return new FriendBeforeGroupInviteRequest(memberId, groupId, size, createdAt);
    }

}
