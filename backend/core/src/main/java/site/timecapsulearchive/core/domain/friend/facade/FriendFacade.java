package site.timecapsulearchive.core.domain.friend.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.friend.service.FriendService;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.service.MemberService;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Component
@RequiredArgsConstructor
public class FriendFacade {

    private final MemberService memberService;
    private final FriendService friendService;
    private final TransactionTemplate transactionTemplate;
    private final SocialNotificationManager socialNotificationManager;

    public void requestFriends(Long memberId, List<Long> friendIds) {
        final Member[] owner = new Member[1];
        final List<Long>[] foundFriendIds = new List[1];

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                owner[0] = memberService.findMemberById(memberId);
                foundFriendIds[0] = memberService.findMemberIdsByIds(friendIds);

                friendService.bulkSaveFriendInvites(owner[0].getId(), foundFriendIds[0]);
            }
        });

        socialNotificationManager.sendFriendRequestMessages(
            owner[0].getNickname(),
            owner[0].getProfileUrl(),
            foundFriendIds[0]
        );
    }
}
