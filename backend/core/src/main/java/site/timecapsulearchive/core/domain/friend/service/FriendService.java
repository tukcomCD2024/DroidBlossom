package site.timecapsulearchive.core.domain.friend.service;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchTagFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.mapper.FriendMapper;
import site.timecapsulearchive.core.domain.friend.data.mapper.MemberFriendMapper;
import site.timecapsulearchive.core.domain.friend.data.response.FriendReqStatusResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendRequestsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.FriendsSliceResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendsResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.friend.exception.DuplicateFriendIdException;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.security.encryption.HashEncryptionManager;
import site.timecapsulearchive.core.infra.notification.manager.NotificationManager;

@Service
public class FriendService {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberFriendQueryRepository memberFriendQueryRepository;
    private final MemberFriendMapper memberFriendMapper;
    private final MemberRepository memberRepository;
    private final FriendInviteRepository friendInviteRepository;
    private final FriendMapper friendMapper;
    private final NotificationManager notificationManager;
    private final TransactionTemplate transactionTemplate;
    private final HashEncryptionManager hashEncryptionManager;

    public FriendService(
        MemberFriendRepository memberFriendRepository,
        MemberFriendQueryRepository memberFriendQueryRepository,
        MemberFriendMapper memberFriendMapper, MemberRepository memberRepository,
        FriendInviteRepository friendInviteRepository,
        FriendMapper friendMapper,
        NotificationManager notificationManager,
        PlatformTransactionManager transactionManager, HashEncryptionManager hashEncryptionManager
    ) {
        this.memberFriendRepository = memberFriendRepository;
        this.memberFriendQueryRepository = memberFriendQueryRepository;
        this.memberFriendMapper = memberFriendMapper;
        this.memberRepository = memberRepository;
        this.friendInviteRepository = friendInviteRepository;
        this.friendMapper = friendMapper;
        this.notificationManager = notificationManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.hashEncryptionManager = hashEncryptionManager;
        this.transactionTemplate.setTimeout(7);
    }

    public FriendReqStatusResponse requestFriend(final Long memberId, final Long friendId) {
        if (memberId.equals(friendId)) {
            throw new DuplicateFriendIdException();
        }
        final Member owner = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        final Member friend = memberRepository.findMemberById(friendId).orElseThrow(
            MemberNotFoundException::new);

        final FriendInvite friendInvite = friendMapper.friendReqToEntity(owner, friend);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                friendInviteRepository.save(friendInvite);
            }
        });

        notificationManager.sendFriendReqMessage(friendId, owner.getNickname());

        return FriendReqStatusResponse.success();
    }

    public void acceptFriend(final Long memberId, final Long friendId) {
        if (memberId.equals(friendId)) {
            throw new DuplicateFriendIdException();
        }
        final FriendInvite friendInvite = friendInviteRepository
            .findFriendInviteWithMembersByOwnerIdAndFriendId(memberId, friendId)
            .orElseThrow(FriendNotFoundException::new);

        final MemberFriend ownerRelation = friendInvite.ownerRelation();
        final MemberFriend friendRelation = friendInvite.friendRelation();

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                friendInviteRepository.delete(friendInvite);
                memberFriendRepository.save(ownerRelation);
                memberFriendRepository.save(friendRelation);
            }
        });

        notificationManager.sendFriendAcceptMessage(friendId, ownerRelation.getOwnerNickname());
    }

    @Transactional
    public void denyRequestFriend(Long memberId, Long friendId) {
        if (memberId.equals(friendId)) {
            throw new DuplicateFriendIdException();
        }
        final FriendInvite friendInvite = friendInviteRepository
            .findFriendInviteByOwnerIdAndFriendId(memberId, friendId).orElseThrow(
                FriendNotFoundException::new);

        friendInviteRepository.deleteFriendInviteById(friendInvite.getId());
    }

    @Transactional
    public void deleteFriend(final Long memberId, final Long friendId) {
        if (memberId.equals(friendId)) {
            throw new DuplicateFriendIdException();
        }
        memberFriendRepository
            .findMemberFriendByOwnerIdAndFriendId(memberId, friendId)
            .forEach(memberFriendRepository::delete);
    }

    @Transactional(readOnly = true)
    public FriendsSliceResponse findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        Slice<FriendSummaryDto> friends = memberFriendQueryRepository.findFriendsSlice(memberId,
            size, createdAt);

        return memberFriendMapper.friendsSliceToResponse(friends);
    }

    @Transactional(readOnly = true)
    public FriendRequestsSliceResponse findFriendRequestsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        Slice<FriendSummaryDto> friendRequests = memberFriendQueryRepository.findFriendRequestsSlice(
            memberId, size, createdAt);

        return memberFriendMapper.friendRequestsSliceToResponse(friendRequests.getContent(),
            friendRequests.hasNext());
    }

    @Transactional(readOnly = true)
    public SearchFriendsResponse findFriendsByPhone(final Long memberId,
        final List<String> phones) {
        final List<byte[]> hashes = phones.stream()
            .map(phone -> hashEncryptionManager.encrypt(phone.getBytes(StandardCharsets.UTF_8)))
            .toList();

        final List<SearchFriendSummaryDto> friends = memberFriendQueryRepository.findFriendsByPhone(
            memberId, hashes);

        return memberFriendMapper.searchFriendSummaryDtosToResponse(friends);
    }

    @Transactional(readOnly = true)
    public SearchTagFriendSummaryResponse searchFriend(final Long memberId, final String tag) {
        final SearchTagFriendSummaryDto friendSummaryDto = memberFriendQueryRepository
            .findFriendsByTag(memberId, tag).orElseThrow(FriendNotFoundException::new);

        return friendSummaryDto.toResponse();
    }
}
