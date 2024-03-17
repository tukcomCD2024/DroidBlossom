package site.timecapsulearchive.core.domain.friend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.data.mapper.FriendMapper;
import site.timecapsulearchive.core.domain.friend.data.response.FriendReqStatusResponse;
import site.timecapsulearchive.core.domain.friend.entity.FriendInvite;
import site.timecapsulearchive.core.domain.friend.entity.MemberFriend;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;
import site.timecapsulearchive.core.domain.friend.repository.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.notification.manager.NotificationManager;

@Service
public class FriendService {

    private final MemberFriendRepository memberFriendRepository;
    private final MemberRepository memberRepository;
    private final FriendInviteRepository friendInviteRepository;
    private final FriendMapper friendMapper;
    private final NotificationManager notificationManager;
    private final TransactionTemplate transactionTemplate;

    public FriendService(
        MemberFriendRepository memberFriendRepository, MemberRepository memberRepository,
        FriendInviteRepository friendInviteRepository,
        FriendMapper friendMapper,
        NotificationManager notificationManager,
        PlatformTransactionManager transactionManager
    ) {
        this.memberFriendRepository = memberFriendRepository;
        this.memberRepository = memberRepository;
        this.friendInviteRepository = friendInviteRepository;
        this.friendMapper = friendMapper;
        this.notificationManager = notificationManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setTimeout(7);
    }

    public FriendReqStatusResponse requestFriend(final Long memberId, final Long friendId) {
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

    @Transactional
    public void denyRequestFriend(Long memberId, Long friendId) {
        final FriendInvite friendInvite = friendInviteRepository
            .findFriendInviteByOwnerIdAndFriendId(memberId, friendId).orElseThrow(
                FriendNotFoundException::new);

        friendInviteRepository.deleteFriendInviteById(friendInvite.getId());
    }
  
    @Transactional
    public void deleteFriend(final Long memberId, final Long friendId) {
        final MemberFriend memberFriend = memberFriendRepository.findMemberFriendByOwnerIdAndFriendId(
                memberId, friendId)
            .orElseThrow(FriendNotFoundException::new);

        memberFriendRepository.delete(memberFriend);
    }
}
