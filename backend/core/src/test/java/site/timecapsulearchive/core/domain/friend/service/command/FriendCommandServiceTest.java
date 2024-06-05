package site.timecapsulearchive.core.domain.friend.service.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.dependency.TestTransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.dto.FriendInviteMemberIdsDtoFixture;
import site.timecapsulearchive.core.domain.friend.exception.FriendInviteDuplicateException;
import site.timecapsulearchive.core.domain.friend.exception.FriendInviteNotFoundException;
import site.timecapsulearchive.core.domain.friend.exception.FriendTwoWayInviteException;
import site.timecapsulearchive.core.domain.friend.exception.SelfFriendOperationException;
import site.timecapsulearchive.core.domain.friend.repository.friend_invite.FriendInviteRepository;
import site.timecapsulearchive.core.domain.friend.repository.member_friend.MemberFriendRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

class FriendCommandServiceTest {

    private final MemberFriendRepository memberFriendRepository = mock(
        MemberFriendRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final FriendInviteRepository friendInviteRepository = mock(
        FriendInviteRepository.class);
    private final SocialNotificationManager socialNotificationManager = mock(
        SocialNotificationManager.class);
    private final TransactionTemplate transactionTemplate = TestTransactionTemplate.spied();

    private final FriendCommandService friendCommandService = new FriendCommandService(
        memberFriendRepository,
        memberRepository,
        friendInviteRepository,
        socialNotificationManager,
        transactionTemplate
    );

    @Test
    void 사용자_본인한테_다건_친구_요청을_보낸_경우_어떠한_동작도_수행하지_않는다() {
        //given
        Long memberId = 1L;
        List<Long> friendIds = List.of(1L, 1L, 1L, 1L);
        given(friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(anyList(),
            any()))
            .willReturn(Collections.emptyList());

        //when
        friendCommandService.requestFriends(memberId, friendIds);

        //then
        verify(transactionTemplate, never()).execute(any());
    }

    @Test
    void 이미_친구요청된_친구에게_다건_친구_요청을_보낸_경우_어떠한_동작도_수행하지_않는다() {
        //given
        Long memberId = 1L;
        List<Long> friendIds = List.of(2L, 3L, 4L, 5L, 6L);
        given(friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(anyList(),
            any()))
            .willReturn(FriendInviteMemberIdsDtoFixture.duplicates(memberId, friendIds));

        //when
        friendCommandService.requestFriends(memberId, friendIds);

        //then
        verify(transactionTemplate, never()).execute(any());
    }

    @Test
    void 양방향_다건_친구_요청을_보낸_경우_어떠한_동작도_수행하지_않는다() {
        //given
        Long memberId = 1L;
        List<Long> friendIds = List.of(2L, 3L, 4L, 5L, 6L);
        given(friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(anyList(),
            any()))
            .willReturn(FriendInviteMemberIdsDtoFixture.twoWays(memberId, friendIds));

        //when
        friendCommandService.requestFriends(memberId, friendIds);

        //then
        verify(transactionTemplate, never()).execute(any());
    }

    @Test
    void 친구_요청을_보낸_경우_트랜잭션이_동작된다() {
        //given
        Long memberId = 1L;
        List<Long> friendIds = List.of(2L, 3L, 4L, 5L, 6L);
        List<Long> subFriendIds = friendIds.subList(0, 3);
        given(friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdsAndFriendId(anyList(),
            any()))
            .willReturn(FriendInviteMemberIdsDtoFixture.twoWays(memberId, subFriendIds));
        given(memberRepository.findMemberById(anyLong())).willReturn(
            Optional.ofNullable(MemberFixture.memberWithMemberId(0L)));

        //when
        friendCommandService.requestFriends(memberId, friendIds);

        //then
        verify(transactionTemplate, times(1)).execute(any());
    }

    @Test
    void 사용자_본인한테_단건_친구_요청을_보낸_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.requestFriend(memberId, memberId))
            .isInstanceOf(SelfFriendOperationException.class)
            .hasMessageContaining(ErrorCode.SELF_FRIEND_OPERATION_ERROR.getMessage());
    }

    @Test
    void 이미_친구요청된_친구에게_단건_친구_요청을_보낸_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long friendId = 2L;
        given(friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdAndFriendId(anyLong(), anyLong()))
            .willReturn(FriendInviteMemberIdsDtoFixture.duplicate(memberId, friendId));

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.requestFriend(memberId, friendId))
            .isInstanceOf(FriendInviteDuplicateException.class)
            .hasMessageContaining(ErrorCode.FRIEND_INVITE_DUPLICATE_ERROR.getMessage());
    }

    @Test
    void 양방향_단건_친구_요청을_보낸_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long friendId = 2L;
        given(friendInviteRepository.findFriendInviteMemberIdsDtoByMemberIdAndFriendId(anyLong(), anyLong()))
            .willReturn(FriendInviteMemberIdsDtoFixture.twoWay(memberId, friendId));

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.requestFriend(memberId, friendId))
            .isInstanceOf(FriendTwoWayInviteException.class)
            .hasMessageContaining(ErrorCode.FRIEND_TWO_WAY_INVITE_ERROR.getMessage());
    }

    @Test
    void 사용자_본인한테_친구_요청을_수락한_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.acceptFriend(memberId, memberId))
            .isInstanceOf(SelfFriendOperationException.class)
            .hasMessageContaining(ErrorCode.SELF_FRIEND_OPERATION_ERROR.getMessage());
    }

    @Test
    void 사용자_본인한테_친구_요청을_거부한_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.denyRequestFriend(memberId, memberId))
            .isInstanceOf(SelfFriendOperationException.class)
            .hasMessageContaining(ErrorCode.SELF_FRIEND_OPERATION_ERROR.getMessage());
    }

    @Test
    void 사용자_본인한테_친구_삭제를_요청한_경우_예외가_발생한다() {
        //given
        Long memberId = 1L;

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.deleteFriend(memberId, memberId))
            .isInstanceOf(SelfFriendOperationException.class)
            .hasMessageContaining(ErrorCode.SELF_FRIEND_OPERATION_ERROR.getMessage());
    }

    @Test
    void 친구_요청을_보낸_사용자가_본인의_아이디로_친구_요청을_삭제하면_예외가_발생한다() {
        //given
        Long memberId = 1L;

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.deleteSendingFriendInvite(memberId, memberId))
            .isInstanceOf(SelfFriendOperationException.class)
            .hasMessageContaining(ErrorCode.SELF_FRIEND_OPERATION_ERROR.getMessage());
    }

    @Test
    void 친구_요청을_보내지_않은_사용자가_친구_요청을_삭제하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long friendId = 2L;
        given(friendInviteRepository.findFriendSendingInviteForUpdateByOwnerIdAndFriendId(anyLong(),
            anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> friendCommandService.deleteSendingFriendInvite(memberId, friendId))
            .isInstanceOf(FriendInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.FRIEND_INVITE_NOT_FOUND_ERROR.getMessage());
    }
}