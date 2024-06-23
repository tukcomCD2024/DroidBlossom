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
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendQueryService {

    private final MemberRepository memberRepository;
    private final MemberFriendRepository memberFriendRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInviteRepository groupInviteRepository;
    private final FriendInviteRepository friendInviteRepository;

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

        final List<Long> groupMemberIdsToExcludeBeforeGroupInvite = getGroupMemberIdsToExcludeBeforeGroupInvite(
            request);

        final List<FriendSummaryDto> friendSummaryBeforeGroupInvitedDtos = getFriendSummaryBeforeGroupInvitedDtos(
            friendSummaryDtos, groupMemberIdsToExcludeBeforeGroupInvite);

        return new SliceImpl<>(friendSummaryBeforeGroupInvitedDtos, friendSummaryDtos.getPageable(),
            friendSummaryDtos.hasNext());
    }

    private List<Long> getGroupMemberIdsToExcludeBeforeGroupInvite(
        final FriendBeforeGroupInviteRequest request) {
        return Stream.concat(
                memberGroupRepository.findGroupMemberIdsByGroupId(request.groupId()).stream(),
                groupInviteRepository.findGroupMemberIdsByGroupIdAndGroupOwnerId(request.groupId(),
                    request.memberId()).stream())
            .distinct()
            .toList();
    }

    private List<FriendSummaryDto> getFriendSummaryBeforeGroupInvitedDtos(
        final Slice<FriendSummaryDto> friendSummaryDtos,
        final List<Long> groupMemberIdsToExcludeBeforeGroupInvite
    ) {
        return friendSummaryDtos.getContent()
            .stream()
            .filter(dto -> !groupMemberIdsToExcludeBeforeGroupInvite.contains(dto.id())).toList();
    }

    public Slice<FriendSummaryDto> findFriendReceivingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return friendInviteRepository.findFriendReceivingInvitesSlice(memberId, size, createdAt);
    }

    public Slice<FriendSummaryDto> findFriendSendingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return friendInviteRepository.findFriendSendingInvitesSlice(memberId, size, createdAt);
    }

    public List<SearchFriendSummaryDto> findFriendsByPhone(
        final Long memberId,
        final List<ByteArrayWrapper> phoneEncryption
    ) {
        final List<byte[]> hashes = phoneEncryption.stream()
            .map(ByteArrayWrapper::getData)
            .toList();

        final byte[] memberPhoneHash = memberRepository.findMemberPhoneHash(memberId).orElseThrow(
            MemberNotFoundException::new);
        final ByteArrayWrapper memberPhoneWrapper = new ByteArrayWrapper(memberPhoneHash);

        final List<SearchFriendSummaryDto> friendSummaryDtos = memberFriendRepository.findFriendsByPhone(
            memberId, hashes);

        friendSummaryDtos.removeIf(dto -> dto.phoneHash().equals(memberPhoneWrapper));

        return friendSummaryDtos;
    }

    public SearchFriendSummaryDtoByTag searchFriend(final Long memberId, final String tag) {
        return memberFriendRepository.findFriendsByTag(memberId, tag)
            .orElseThrow(FriendNotFoundException::new);
    }
}
