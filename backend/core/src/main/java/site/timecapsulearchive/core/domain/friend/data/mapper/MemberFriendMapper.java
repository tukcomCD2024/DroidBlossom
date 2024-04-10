package site.timecapsulearchive.core.domain.friend.data.mapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.global.security.encryption.AESEncryptionManager;

@Component
@RequiredArgsConstructor
public class MemberFriendMapper {

    private final AESEncryptionManager aesEncryptionManager;

    public FriendsSliceResponse friendsSliceToResponse(final Slice<FriendSummaryDto> slice) {
        final List<FriendSummaryResponse> friends = slice.getContent()
            .stream()
            .map(this::friendsSummaryDtoToResponse)
            .toList();

        return new FriendsSliceResponse(friends, slice.hasNext());
    }

    private FriendSummaryResponse friendsSummaryDtoToResponse(final FriendSummaryDto dto) {
        return FriendSummaryResponse.builder()
            .id(dto.id())
            .profileUrl(dto.profileUrl())
            .nickname(dto.nickname())
            .createdAt(dto.createdAt())
            .build();
    }

    public FriendRequestsSliceResponse friendRequestsSliceToResponse(
        final List<FriendSummaryDto> content,
        final boolean hasNext
    ) {
        List<FriendSummaryResponse> friendRequests = content.stream()
            .map(this::friendsSummaryDtoToResponse)
            .toList();

        return new FriendRequestsSliceResponse(friendRequests, hasNext);
    }

    public SearchFriendsResponse searchFriendSummaryDtosToResponse(
        final List<SearchFriendSummaryDto> dtos) {
        return new SearchFriendsResponse(dtos.stream()
            .map(this::searchFriendSummaryDtoToResponse)
            .toList()
        );
    }

    public SearchFriendSummaryResponse searchFriendSummaryDtoToResponse(
        final SearchFriendSummaryDto dto) {
        return SearchFriendSummaryResponse.builder()
            .id(dto.id())
            .profileUrl(dto.profileUrl())
            .nickname(dto.nickname())
            .phone(aesEncryptionManager.decryptWithPrefixIV(dto.phone()))
            .isFriend(dto.isFriend())
            .isFriendRequest(dto.isFriendRequest())
            .build();
    }
}
