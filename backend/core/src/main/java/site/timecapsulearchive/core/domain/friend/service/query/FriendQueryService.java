package site.timecapsulearchive.core.domain.friend.service.query;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.domain.friend.data.request.FriendBeforeGroupInviteRequest;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendQueryService {

    private final MemberFriendRepository memberFriendRepository;

    public Slice<FriendSummaryDto> findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return memberFriendRepository.findFriendsSlice(memberId, size, createdAt);
    }

    public Slice<FriendSummaryDto> findFriendsBeforeGroupInviteSlice(
        final FriendBeforeGroupInviteRequest request) {
        return memberFriendRepository.findFriendsBeforeGroupInvite(request);
    }

    public Slice<FriendSummaryDto> findFriendRequestsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return memberFriendRepository.findFriendRequestsSlice(memberId, size, createdAt);
    }

    public List<SearchFriendSummaryDto> findFriendsByPhone(
        final Long memberId,
        final List<ByteArrayWrapper> phoneEncryption
    ) {
        final List<byte[]> hashes = phoneEncryption.stream().map(ByteArrayWrapper::getData)
            .toList();

        return memberFriendRepository.findFriendsByPhone(memberId, hashes);
    }

    public SearchTagFriendSummaryResponse searchFriend(final Long memberId, final String tag) {
        final SearchFriendSummaryDtoByTag friendSummaryDto = memberFriendRepository
            .findFriendsByTag(memberId, tag).orElseThrow(FriendNotFoundException::new);

        return friendSummaryDto.toResponse();
    }


}
