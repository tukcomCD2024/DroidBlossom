package site.timecapsulearchive.core.domain.group_member.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.common.fixture.dto.GroupDtoFixture;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group_member.data.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group_member.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group_member.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.group_member.exception.GroupMemberDuplicatedIdException;
import site.timecapsulearchive.core.domain.group_member.exception.GroupMemberNotFoundException;
import site.timecapsulearchive.core.domain.group_member.exception.GroupQuitException;
import site.timecapsulearchive.core.domain.group_member.exception.NoGroupAuthorityException;
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

    private final GroupMemberCommandService groupMemberCommandService = new GroupMemberCommandService(
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
        groupMemberCommandService.inviteGroup(memberId, groupId, targetId);

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
        assertThatThrownBy(() -> groupMemberCommandService.inviteGroup(memberId, groupId, targetId))
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
        assertThatThrownBy(() -> groupMemberCommandService.inviteGroup(memberId, groupId, targetId))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
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
        assertThatCode(
            () -> groupMemberCommandService.rejectRequestGroup(memberId, groupId, targetId))
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
            () -> groupMemberCommandService.rejectRequestGroup(memberId, groupId, targetId))
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
        groupMemberCommandService.acceptGroupInvite(memberId, groupId, targetId);

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
        assertThatThrownBy(
            () -> groupMemberCommandService.acceptGroupInvite(memberId, groupId, targetId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
    }


    @Test
    void 그룹장인_사용자가_그룹_탈퇴를_시도하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        given(memberGroupRepository.findMemberGroupByMemberIdAndGroupId(groupOwnerId,
            groupId)).willReturn(ownerGroupMemberOnly());

        //when
        //then
        assertThatThrownBy(() -> groupMemberCommandService.quitGroup(groupOwnerId, groupId))
            .isInstanceOf(GroupQuitException.class)
            .hasMessageContaining(ErrorCode.GROUP_OWNER_QUIT_ERROR.getMessage());
    }

    private Optional<MemberGroup> ownerGroupMemberOnly() {
        return Optional.of(
            MemberGroupFixture.groupOwner(MemberFixture.memberWithMemberId(1L),
                GroupFixture.group())
        );
    }

    @Test
    void 그룹장이_아닌_사용자가_그룹_탈퇴를_시도하면_그룹을_탈퇴한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        given(memberGroupRepository.findMemberGroupByMemberIdAndGroupId(groupOwnerId,
            groupId)).willReturn(notOwnerGroupMemberOnly());

        //when
        groupMemberCommandService.quitGroup(groupOwnerId, groupId);

        //then
        verify(memberGroupRepository, times(1)).delete(any(MemberGroup.class));
    }

    private Optional<MemberGroup> notOwnerGroupMemberOnly() {
        return Optional.of(
            MemberGroupFixture.memberGroup(
                MemberFixture.memberWithMemberId(2L),
                GroupFixture.group(),
                false
            )
        );
    }

    @Test
    void 나_자신을_그룹에서_삭제하려하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;

        //when
        //then
        assertThatThrownBy(
            () -> groupMemberCommandService.kickGroupMember(groupOwnerId, groupId, groupOwnerId))
            .isInstanceOf(GroupMemberDuplicatedIdException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_DUPLICATED_ID_ERROR.getMessage());
    }

    @Test
    void 그룹_삭제를_요청한_사용자를_그룹에서_찾을_수_없으면_예외가_발생한다() {
        //given
        Long notExistGroupOwnerId = 1L;
        Long groupId = 1L;
        Long groupMemberId = 2L;
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupMemberCommandService.kickGroupMember(notExistGroupOwnerId, groupId, groupMemberId))
            .isInstanceOf(GroupMemberNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹_삭제를_요청한_사용자가_그룹에서_그룹장이_아니면_예외가_발생한다() {
        //given
        Long notGroupOwnerId = 1L;
        Long groupId = 1L;
        Long groupMemberId = 2L;
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.of(Boolean.FALSE));

        //when
        //then
        assertThatThrownBy(
            () -> groupMemberCommandService.kickGroupMember(notGroupOwnerId, groupId, groupMemberId))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹_삭제의_대상_그룹원이_그룹에_존재하지_않으면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        Long notExistGroupMemberId = 2L;
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.of(Boolean.TRUE));
        given(memberGroupRepository.findMemberGroupByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupMemberCommandService.kickGroupMember(groupOwnerId, groupId, notExistGroupMemberId))
            .isInstanceOf(GroupMemberNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹장이_그룹원을_삭제하면_그룹에서_삭제된다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        Long groupMemberId = 2L;
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.of(Boolean.TRUE));
        given(memberGroupRepository.findMemberGroupByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(notOwnerGroupMemberOnly());

        //when
        groupMemberCommandService.kickGroupMember(groupOwnerId, groupId, groupMemberId);

        //then
        verify(memberGroupRepository, times(1)).delete(any(MemberGroup.class));
    }
}
