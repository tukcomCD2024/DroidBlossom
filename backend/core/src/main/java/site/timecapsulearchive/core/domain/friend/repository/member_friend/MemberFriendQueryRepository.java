package site.timecapsulearchive.core.domain.friend.repository.member_friend;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.domain.friend.data.request.FriendBeforeGroupInviteRequest;


public interface MemberFriendQueryRepository {

    Slice<FriendSummaryDto> findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    List<SearchFriendSummaryDto> findFriendsByPhone(
        final Long memberId,
        final List<byte[]> hashes
    );

    Optional<SearchFriendSummaryDtoByTag> findFriendsByTag(
        final Long memberId,
        final String tag
    );

    List<Long> findFriendIdsByOwnerId(final Long memberId);

    Slice<FriendSummaryDto> findFriendsBeforeGroupInvite(
        final FriendBeforeGroupInviteRequest request);

    List<Long> findFriendIds(final List<Long> groupMemberIds, final Long memberId);
}
