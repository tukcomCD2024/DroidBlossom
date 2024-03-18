package site.timecapsulearchive.core.domain.friend.data.mapper;

import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.member.entity.Member;

@Component
public class MemberFriendMapper {

    public FriendsSliceResponse friendsSliceToResponse(Slice<FriendSummaryDto> slice) {
        List<FriendSummaryResponse> friends = slice.getContent()
            .stream()
            .map(this::friendsSummaryDtoToResponse)
            .toList();

        return new FriendsSliceResponse(friends, slice.hasNext());
    }

    private FriendSummaryResponse friendsSummaryDtoToResponse(FriendSummaryDto dto) {
        return new FriendSummaryResponse(dto.id(), dto.profileUrl(), dto.nickname(),
            dto.createdAt());
    }

    public FriendRequestsSliceResponse friendRequestsSliceToResponse(
        List<FriendSummaryDto> content,
        boolean hasNext
    ) {
        List<FriendSummaryResponse> friendRequests = content.stream()
            .map(this::friendsSummaryDtoToResponse)
            .toList();

        return new FriendRequestsSliceResponse(friendRequests, hasNext);
    }

    public MemberFriend memberToEntity(Member owner, Member friend) {
        return MemberFriend.builder()
            .owner(owner)
            .friend(friend)
            .build();
    }
}
