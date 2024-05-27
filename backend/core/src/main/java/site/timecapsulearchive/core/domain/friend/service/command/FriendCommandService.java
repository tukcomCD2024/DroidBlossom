package site.timecapsulearchive.core.domain.friend.service.command;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.friend.exception.FriendDuplicateIdException;
import site.timecapsulearchive.core.domain.friend.exception.FriendInviteNotFoundException;
import site.timecapsulearchive.core.domain.friend.exception.FriendTwoWayInviteException;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Service
@RequiredArgsConstructor
public class FriendCommandService {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberRepository memberRepository;
    private final FriendInviteRepository friendInviteRepository;
    private final SocialNotificationManager socialNotificationManager;
    private final TransactionTemplate transactionTemplate;

    public void requestFriends(Long memberId, List<Long> friendIds) {
        final Member[] owner = new Member[1];
        final List<Long>[] foundFriendIds = new List[1];

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                owner[0] = memberRepository.findMemberById(memberId)
                    .orElseThrow(MemberNotFoundException::new);
                foundFriendIds[0] = memberRepository.findMemberIdsByIds(friendIds);

                friendInviteRepository.bulkSave(owner[0].getId(), foundFriendIds[0]);
            }
        });

        socialNotificationManager.sendFriendRequestMessages(
            owner[0].getNickname(),
            owner[0].getProfileUrl(),
            foundFriendIds[0]
        );
    }

    public void requestFriend(final Long memberId, final Long friendId) {
        validateFriendDuplicateId(memberId, friendId);
        validateTwoWayInvite(memberId, friendId);

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

    private void validateFriendDuplicateId(final Long memberId, final Long friendId) {
        if (memberId.equals(friendId)) {
            throw new FriendDuplicateIdException();
        }
    }

    private void validateTwoWayInvite(final Long memberId, final Long friendId) {
        final Optional<FriendInvite> friendInvite = friendInviteRepository.findFriendInviteByOwnerIdAndFriendId(
            friendId, memberId);

        if (friendInvite.isPresent()) {
            throw new FriendTwoWayInviteException();
        }
    }


    public void acceptFriend(final Long memberId, final Long friendId) {
        validateFriendDuplicateId(memberId, friendId);

        final String[] ownerNickname = new String[1];
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final List<FriendInvite> friendInvites = friendInviteRepository
                    .findFriendInviteWithMembersByOwnerIdAndFriendId(memberId, friendId);

                if (friendInvites.isEmpty()) {
                    throw new FriendInviteNotFoundException();
                }

                final FriendInvite friendInvite = friendInvites.get(0);

                final MemberFriend ownerRelation = friendInvite.ownerRelation();
                ownerNickname[0] = ownerRelation.getOwnerNickname();

                final MemberFriend friendRelation = friendInvite.friendRelation();

                memberFriendRepository.save(ownerRelation);
                memberFriendRepository.save(friendRelation);
                friendInvites.forEach(friendInviteRepository::delete);
            }
        });

        socialNotificationManager.sendFriendAcceptMessage(ownerNickname[0], friendId);
    }

    @Transactional
    public void denyRequestFriend(final Long memberId, final Long friendId) {
        validateFriendDuplicateId(memberId, friendId);

        int isDenyRequest = friendInviteRepository.deleteFriendInviteByOwnerIdAndFriendId(friendId,
            memberId);

        if (isDenyRequest != 1) {
            throw new FriendTwoWayInviteException();
        }
    }

    @Transactional
    public void deleteFriend(final Long memberId, final Long friendId) {
        validateFriendDuplicateId(memberId, friendId);

        final List<MemberFriend> memberFriends = memberFriendRepository
            .findMemberFriendByOwnerIdAndFriendId(memberId, friendId);

        memberFriends.forEach(memberFriendRepository::delete);
    }

}
