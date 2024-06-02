package site.timecapsulearchive.core.domain.friend.repository.friend_invite;

import java.util.List;
import java.util.Optional;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteMemberIdsDto;

public interface FriendInviteQueryRepository {

    void bulkSave(final Long ownerId, final List<Long> friendIds);

    List<FriendInviteMemberIdsDto> findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(List<Long> memberIds, Long friendId);

    Optional<FriendInviteMemberIdsDto> findFriendInviteMemberIdsDtoByMemberIdAndFriendId(Long memberId, Long friendId);
}
