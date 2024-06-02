package site.timecapsulearchive.core.domain.friend.data.dto;

import java.util.Objects;

public record FriendInviteMemberIdsDto(
    Long ownerId,
    Long friendId
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FriendInviteMemberIdsDto that = (FriendInviteMemberIdsDto) o;
        return (Objects.equals(ownerId, that.ownerId) && Objects.equals(friendId, that.friendId))
            || (Objects.equals(ownerId, that.friendId) && Objects.equals(friendId, that.ownerId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, friendId);
    }
}
