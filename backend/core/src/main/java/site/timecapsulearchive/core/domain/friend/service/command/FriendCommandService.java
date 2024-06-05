package site.timecapsulearchive.core.domain.friend.service.command;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteMemberIdsDto;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendInviteNotificationDto;
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
        List<Long> filteredFriendIds = filterTwoWayAndDuplicateInvite(memberId, friendIds);

        if (filteredFriendIds.isEmpty()) {
            return;
        }

        final FriendInviteNotificationDto dto = transactionTemplate.execute(status -> {
            Member owner = memberRepository.findMemberById(memberId)
                .orElseThrow(MemberNotFoundException::new);
            List<Long> foundFriendIds = memberRepository.findMemberIdsByIds(friendIds);

            friendInviteRepository.bulkSave(owner.getId(), foundFriendIds);

            return FriendInviteNotificationDto.create(owner.getNickname(), owner.getProfileUrl(),
                foundFriendIds);
        });

        socialNotificationManager.sendFriendRequestMessages(
            dto.nickname(),
            dto.profileUrl(),
            dto.foundFriendIds()
        );
    }

    private List<Long> filterTwoWayAndDuplicateInvite(Long memberId, List<Long> friendIds) {
        List<FriendInviteMemberIdsDto> twoWayIds = friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(
            friendIds, memberId);

        return friendIds.stream()
            .filter(id -> !memberId.equals(id))
            .filter(isNotTwoWayInvite(memberId, twoWayIds))
            .toList();
    }

    private Predicate<Long> isNotTwoWayInvite(Long memberId,
        List<FriendInviteMemberIdsDto> twoWayIds) {
        return id -> twoWayIds.stream()
            .noneMatch(twoWayId ->
                (twoWayId.ownerId().equals(id) && twoWayId.friendId().equals(memberId)) ||
                    (twoWayId.ownerId().equals(memberId) && twoWayId.friendId().equals(id))
            );
    }

    public void requestFriend(final Long memberId, final Long friendId) {
        validateSelfFriendOperation(memberId, friendId);
        validateTwoWayAndDuplicateInvite(memberId, friendId);

        final Member owner = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        final Member friend = memberRepository.findMemberById(friendId).orElseThrow(
            MemberNotFoundException::new);

        final FriendInvite createfriendInvite = FriendInvite.createOf(owner, friend);

        transactionTemplate.executeWithoutResult(status ->
            friendInviteRepository.save(createfriendInvite)
        );

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
            final FriendInvite friendInvite = friendInviteRepository.findFriendReceivingInviteForUpdateByOwnerIdAndFriendId(
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
