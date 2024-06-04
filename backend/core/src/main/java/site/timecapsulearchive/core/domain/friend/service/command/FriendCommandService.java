package site.timecapsulearchive.core.domain.friend.service.command;

import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteMemberIdsDto;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.friend.exception.FriendInviteDuplicateException;
import site.timecapsulearchive.core.domain.friend.exception.FriendInviteNotFoundException;
import site.timecapsulearchive.core.domain.friend.exception.FriendTwoWayInviteException;
import site.timecapsulearchive.core.domain.friend.exception.SelfFriendOperationException;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.exception.InternalServerException;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendCommandService {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberRepository memberRepository;
    private final FriendInviteRepository friendInviteRepository;
    private final SocialNotificationManager socialNotificationManager;
    private final TransactionTemplate transactionTemplate;

    public void requestFriends(Long memberId, List<Long> friendIds) {
        List<Long> filteredFriendIds = filterTwoWayAndDuplicateInvite(memberId, friendIds);

        if (filteredFriendIds.isEmpty()) {
            return;
        }

        final Member[] owner = new Member[1];
        final List<Long>[] foundFriendIds = new List[1];

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                owner[0] = memberRepository.findMemberById(memberId)
                    .orElseThrow(MemberNotFoundException::new);
                foundFriendIds[0] = memberRepository.findMemberIdsByIds(filteredFriendIds);

                friendInviteRepository.bulkSave(owner[0].getId(), foundFriendIds[0]);
            }
        });

        socialNotificationManager.sendFriendRequestMessages(
            owner[0].getNickname(),
            owner[0].getProfileUrl(),
            foundFriendIds[0]
        );
    }

    private List<Long> filterTwoWayAndDuplicateInvite(Long memberId, List<Long> friendIds) {
        List<FriendInviteMemberIdsDto> twoWayIds = friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(
            friendIds, memberId);

        return friendIds.stream()
            .filter(id -> !memberId.equals(id))
            .filter(id -> !twoWayIds.contains(new FriendInviteMemberIdsDto(id, memberId)))
            .toList();
    }

    public void requestFriend(final Long memberId, final Long friendId) {
        validateSelfFriendOperation(memberId, friendId);
        validateTwoWayAndDuplicateInvite(memberId, friendId);

        final Member owner = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        final Member friend = memberRepository.findMemberById(friendId).orElseThrow(
            MemberNotFoundException::new);

        final FriendInvite createfriendInvite = FriendInvite.createOf(owner, friend);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                friendInviteRepository.save(createfriendInvite);
            }
        });

        socialNotificationManager.sendFriendReqMessage(owner.getNickname(), friendId);
    }

    private void validateSelfFriendOperation(final Long memberId, final Long friendId) {
        if (memberId.equals(friendId)) {
            throw new SelfFriendOperationException();
        }
    }

    private void validateTwoWayAndDuplicateInvite(final Long memberId, final Long friendId) {
        final Optional<FriendInviteMemberIdsDto> friendInvite = friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdAndFriendId(
            memberId, friendId);

        friendInvite.ifPresent(dto -> {
            if (dto.ownerId().equals(friendId) && dto.friendId().equals(memberId)) {
                throw new FriendTwoWayInviteException();
            } else {
                throw new FriendInviteDuplicateException();
            }
        });
    }


    public void acceptFriend(final Long memberId, final Long ownerId) {
        validateSelfFriendOperation(memberId, ownerId);

        final String ownerNickname = transactionTemplate.execute(status -> {
            FriendInvite friendInvite = friendInviteRepository.findFriendReceivingInviteForUpdateByOwnerIdAndFriendId(
                   ownerId, memberId)
                .orElseThrow(FriendInviteNotFoundException::new);

            final MemberFriend ownerRelation = friendInvite.ownerRelation();
            final MemberFriend friendRelation = friendInvite.friendRelation();

            memberFriendRepository.save(ownerRelation);
            memberFriendRepository.save(friendRelation);

            friendInviteRepository.delete(friendInvite);

            return ownerRelation.getOwnerNickname();
        });

        socialNotificationManager.sendFriendAcceptMessage(ownerNickname, ownerId);
    }

    @Transactional
    public void denyRequestFriend(final Long memberId, final Long ownerId) {
        validateSelfFriendOperation(memberId, ownerId);

        FriendInvite friendInvite = friendInviteRepository.findFriendReceivingInviteForUpdateByOwnerIdAndFriendId(
                ownerId, memberId)
            .orElseThrow(FriendInviteNotFoundException::new);

        friendInviteRepository.delete(friendInvite);
    }

    @Transactional
    public void deleteFriend(final Long memberId, final Long friendId) {
        validateSelfFriendOperation(memberId, friendId);

        final List<MemberFriend> memberFriends = memberFriendRepository
            .findMemberFriendByOwnerIdAndFriendId(memberId, friendId);

        memberFriends.forEach(memberFriendRepository::delete);
    }

    @Transactional
    public void deleteSendingFriendInvite(final Long memberId, final Long friendId) {
        validateSelfFriendOperation(memberId, friendId);

        FriendInvite friendInvite = friendInviteRepository.findFriendSendingInviteForUpdateByOwnerIdAndFriendId(
                memberId, friendId)
            .orElseThrow(FriendInviteNotFoundException::new);

        friendInviteRepository.delete(friendInvite);
    }
}
