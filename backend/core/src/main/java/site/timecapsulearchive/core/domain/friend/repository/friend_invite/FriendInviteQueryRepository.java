package site.timecapsulearchive.core.domain.friend.repository.friend_invite;

import java.util.List;

public interface FriendInviteQueryRepository {

    void bulkSave(final Long ownerId, final List<Long> friendIds);
}
