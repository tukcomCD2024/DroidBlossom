package site.timecapsulearchive.core.domain.friend.service;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchTagFriendSummaryDto;
import site.timecapsulearchive.core.domain.friend.data.response.FriendReqStatusResponse;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.friend.exception.DuplicateFriendIdException;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendQueryRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;
import site.timecapsulearchive.core.infra.notification.manager.NotificationManager;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberFriendQueryRepository memberFriendQueryRepository;
    private final MemberRepository memberRepository;
    private final FriendInviteRepository friendInviteRepository;
    private final FriendInviteQueryRepository friendInviteQueryRepository;
    private final NotificationManager notificationManager;
    private final TransactionTemplate transactionTemplate;

    public FriendReqStatusResponse requestFriend(final Long memberId, final Long friendId) {
        if (memberId.equals(friendId)) {
            throw new DuplicateFriendIdException();
        }
        final Member owner = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        final Member friend = memberRepository.findMemberById(friendId).orElseThrow(
            MemberNotFoundException::new);

        final FriendInvite friendInvite = FriendInvite.createOf(owner, friend);

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

        final String[] ownerNickname = new String[1];
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final FriendInvite friendInvite = friendInviteRepository
                    .findFriendInviteWithMembersByOwnerIdAndFriendId(memberId, friendId)
                    .orElseThrow(FriendNotFoundException::new);

                final MemberFriend ownerRelation = friendInvite.ownerRelation();
                ownerNickname[0] = ownerRelation.getOwnerNickname();

                final MemberFriend friendRelation = friendInvite.friendRelation();

                friendInviteRepository.delete(friendInvite);
                memberFriendRepository.save(ownerRelation);
                memberFriendRepository.save(friendRelation);
            }
        });

        notificationManager.sendFriendAcceptMessage(friendId, ownerNickname[0]);
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
    public Slice<FriendSummaryDto> findFriendsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return memberFriendQueryRepository.findFriendsSlice(memberId, size, createdAt);
    }

    @Transactional(readOnly = true)
    public Slice<FriendSummaryDto> findFriendRequestsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return memberFriendQueryRepository.findFriendRequestsSlice(memberId, size, createdAt);
    }

    @Transactional(readOnly = true)
    public List<SearchFriendSummaryDto> findFriendsByPhone(
        final Long memberId,
        final List<ByteArrayWrapper> phoneEncryption
    ) {
        final List<byte[]> hashes = phoneEncryption.stream().map(ByteArrayWrapper::getData)
            .toList();

        return memberFriendQueryRepository.findFriendsByPhone(memberId, hashes);
    }


    @Transactional(readOnly = true)
    public SearchTagFriendSummaryResponse searchFriend(final Long memberId, final String tag) {
        final SearchTagFriendSummaryDto friendSummaryDto = memberFriendQueryRepository
            .findFriendsByTag(memberId, tag).orElseThrow(FriendNotFoundException::new);

        return friendSummaryDto.toResponse();
    }

    @Transactional
    public void bulkSaveFriendInvites(Long ownerId, List<Long> friendIds) {
        if (friendIds.isEmpty()) {
            return;
        }

        friendInviteQueryRepository.bulkSave(ownerId, friendIds);
    }
}
