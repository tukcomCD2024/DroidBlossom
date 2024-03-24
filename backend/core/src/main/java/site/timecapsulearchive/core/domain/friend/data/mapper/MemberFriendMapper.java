package site.timecapsulearchive.core.domain.friend.data.mapper;

import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendSearchResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
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
        return FriendSummaryResponse.builder()
            .id(dto.id())
            .profileUrl(dto.profileUrl())
            .nickname(dto.nickname())
            .createdAt(dto.createdAt())
            .build();
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

    public SearchFriendsResponse searchFriendSummaryDtosToResponse(
        List<SearchFriendSummaryDto> dtos) {
        return new SearchFriendsResponse(dtos.stream()
            .map(this::searchFriendSummaryDtoToResponse)
            .toList()
        );
    }

    private SearchFriendSummaryResponse searchFriendSummaryDtoToResponse(
        SearchFriendSummaryDto dto) {
        return new SearchFriendSummaryResponse(dto.id(), dto.profileUrl(), dto.nickname(),
            dto.isFriend());
    }

    public FriendSearchResponse friendSearchDtoToResponse(final Member friend, boolean isFriend) {
        return FriendSearchResponse.builder()
            .id(friend.getId())
            .profileUrl(friend.getProfileUrl())
            .nickname(friend.getNickname())
            .isFriend(isFriend)
            .build();
    }
}
