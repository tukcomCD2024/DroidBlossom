package site.timecapsulearchive.core.domain.group_member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.dependency.TestTransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.dto.GroupDtoFixture;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group_member.data.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group_member.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.group_member.exception.GroupOwnerAuthenticateException;
import site.timecapsulearchive.core.domain.group_member.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.group_member.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

public class GroupMemberCommandServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupInviteRepository groupInviteRepository = mock(GroupInviteRepository.class);
    private final SocialNotificationManager socialNotificationManager = mock(
        SocialNotificationManager.class);
    private final TransactionTemplate transactionTemplate = TestTransactionTemplate.spied();

    private final GroupMemberCommandService groupQueryService = new GroupMemberCommandService(
        memberRepository,
        groupRepository,
        memberGroupRepository,
        groupInviteRepository,
        transactionTemplate,
        socialNotificationManager
    );


    @Test
    void 그룹장이_그룹원에게_그룹초대를_하면_그룹초대_알림이_요청된다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;
        Member groupOwner = MemberFixture.member(1);
        GroupOwnerSummaryDto groupOwnerSummaryDto = GroupDtoFixture.groupOwnerSummaryDto(true);

        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupOwner));
        given(memberRepository.findMemberById(targetId)).willReturn(
            Optional.of(MemberFixture.member(2)));
        given(groupRepository.findGroupById(groupId)).willReturn(Optional.of(GroupFixture.group()));
        given(memberGroupRepository.findOwnerInMemberGroup(groupId, memberId)).willReturn(
            Optional.of(groupOwnerSummaryDto));

        //when
        groupQueryService.inviteGroup(memberId, groupId, targetId);

        //then
        verify(socialNotificationManager, times(1)).sendGroupInviteMessage(anyString(), anyString(),
            anyList());
    }

    @Test
    void 그룹장이_그룹초대를_할_때_존재하지_않은_그룹_아이디_이면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;
        Member groupOwner = MemberFixture.member(1);

        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupOwner));
        given(memberRepository.findMemberById(targetId)).willReturn(
            Optional.of(MemberFixture.member(2)));
        given(memberGroupRepository.findOwnerInMemberGroup(groupId, memberId)).willReturn(
            Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupQueryService.inviteGroup(memberId, groupId, targetId))
            .isInstanceOf(GroupNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹장이_아닌_사용자가_그룹원에게_그룹초대를_하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;
        Member groupOwner = MemberFixture.member(1);
        GroupOwnerSummaryDto groupOwnerSummaryDto = GroupDtoFixture.groupOwnerSummaryDto(false);

        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupOwner));
        given(memberRepository.findMemberById(targetId)).willReturn(
            Optional.of(MemberFixture.member(2)));
        given(groupRepository.findGroupById(groupId)).willReturn(Optional.of(GroupFixture.group()));
        given(memberGroupRepository.findOwnerInMemberGroup(groupId, memberId)).willReturn(
            Optional.of(groupOwnerSummaryDto));

        //when
        //then
        assertThatThrownBy(() -> groupQueryService.inviteGroup(memberId, groupId, targetId))
            .isInstanceOf(GroupOwnerAuthenticateException.class)
            .hasMessageContaining(ErrorCode.GROUP_OWNER_AUTHENTICATE_ERROR.getMessage());
    }

    @Test
    void 그룹원은_그룹초대_삭제에서_1을_반환하면_거부할_수_있다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;

        given(groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, targetId, memberId)).willReturn(1);

        //when
        // then
        assertThatCode(() -> groupQueryService.rejectRequestGroup(memberId, groupId, targetId))
            .doesNotThrowAnyException();
    }

    @Test
    void 그룹원은_그룹초대_삭제에서_0을_반환하면_거부가_실패_한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;

        given(groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, targetId, memberId)).willReturn(0);

        //when
        // then
        assertThatThrownBy(
            () -> groupQueryService.rejectRequestGroup(memberId, groupId, targetId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹원은_그룹초대를_수락하면_그룹장에게_알림이_전송된다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;
        Member groupMember = MemberFixture.member(1);

        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupMember));
        given(groupRepository.findGroupById(groupId)).willReturn(
            Optional.of(GroupFixture.group()));
        given(groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, targetId, memberId)).willReturn(1);

        //when
        groupQueryService.acceptGroupInvite(memberId, groupId, targetId);

        //then
        verify(socialNotificationManager, times(1)).sendGroupAcceptMessage(anyString(), anyLong());
    }

    @Test
    void 그룹원은_그룹초대를_수락할_때_그룹초대가_존재하지_않으면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Long targetId = 2L;
        Member groupMember = MemberFixture.member(1);

        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupMember));
        given(groupRepository.findGroupById(groupId)).willReturn(
            Optional.of(GroupFixture.group()));
        given(groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, targetId, memberId)).willReturn(0);

        //when
        //then
        assertThatThrownBy(() -> groupQueryService.acceptGroupInvite(memberId, groupId, targetId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
    }

}
