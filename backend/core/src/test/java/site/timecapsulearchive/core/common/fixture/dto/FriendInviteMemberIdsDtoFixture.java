package site.timecapsulearchive.core.common.fixture.dto;

import java.util.List;
import java.util.Optional;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteMemberIdsDto;

public class FriendInviteMemberIdsDtoFixture {

    public static List<FriendInviteMemberIdsDto> duplicates(
        final Long memberId,
        final List<Long> friendIds
    ) {
        return friendIds.stream()
            .map(friendId -> new FriendInviteMemberIdsDto(memberId, friendId))
            .toList();
    }

    public static List<FriendInviteMemberIdsDto> twoWays(
        final Long memberId,
        final List<Long> friendIds
    ) {
        return friendIds.stream()
            .map(friendId -> new FriendInviteMemberIdsDto(friendId, memberId))
            .toList();
    }

    public static Optional<FriendInviteMemberIdsDto> duplicate(
        final Long memberId,
        final Long friendId
    ) {
        return Optional.of(
            new FriendInviteMemberIdsDto(memberId, friendId)
        );
    }

    public static Optional<FriendInviteMemberIdsDto> twoWay(Long memberId, Long friendId) {
        return Optional.of(
            new FriendInviteMemberIdsDto(friendId, memberId)
        );
    }
}
