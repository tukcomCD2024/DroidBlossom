package site.timecapsulearchive.core.domain.friend.service.query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDtoByTag;
import site.timecapsulearchive.core.domain.friend.data.request.FriendBeforeGroupInviteRequest;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendQueryService {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInviteRepository groupInviteRepository;

    public Slice<FriendSummaryDto> findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return memberFriendRepository.findFriendsSlice(memberId, size, createdAt);
    }

    public Slice<FriendSummaryDto> findFriendsBeforeGroupInviteSlice(
        final FriendBeforeGroupInviteRequest request) {
        final Slice<FriendSummaryDto> friendSummaryDtos = memberFriendRepository.findFriends(
            request);

        final List<Long> groupMemberIdsToExcludeBeforeGroupInvite = Stream.concat(
                memberGroupRepository.getGroupMemberIdsByGroupId(request.groupId()).stream(),
                groupInviteRepository.getGroupMemberIdsByGroupIdAndGroupOwnerId(request.groupId(),
                    request.memberId()).stream())
            .distinct()
            .toList();

        final List<FriendSummaryDto> friendSummaryBeforeGroupInvitedDtos = friendSummaryDtos.getContent()
            .stream()
            .filter(dto -> !groupMemberIdsToExcludeBeforeGroupInvite.contains(dto.id())).toList();

        return new SliceImpl<>(friendSummaryBeforeGroupInvitedDtos, friendSummaryDtos.getPageable(),
            friendSummaryDtos.hasNext());
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

    public SearchFriendSummaryDtoByTag searchFriend(final Long memberId, final String tag) {
        return memberFriendRepository.findFriendsByTag(memberId, tag)
            .orElseThrow(FriendNotFoundException::new);
    }
}
