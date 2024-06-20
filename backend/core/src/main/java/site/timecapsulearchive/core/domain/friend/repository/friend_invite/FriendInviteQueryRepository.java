package site.timecapsulearchive.core.domain.friend.repository.friend_invite;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteMemberIdsDto;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;

public interface FriendInviteQueryRepository {

    void bulkSave(final Long ownerId, final List<Long> friendIds);


    Slice<FriendSummaryDto> findFriendReceivingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    Slice<FriendSummaryDto> findFriendSendingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    List<FriendInviteMemberIdsDto> findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(
        List<Long> memberIds, Long friendId);

    Optional<FriendInviteMemberIdsDto> findFriendInviteMemberIdsDtoByMemberIdAndFriendId(
        Long memberId, Long friendId);
}
