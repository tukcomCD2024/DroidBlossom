package site.timecapsulearchive.core.domain.member_group.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.dependency.TestTransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.GroupInviteFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.common.fixture.dto.GroupDtoFixture;
import site.timecapsulearchive.core.common.fixture.dto.MemberGroupDtoFixture;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.request.SendGroupRequest;
import site.timecapsulearchive.core.domain.member_group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member_group.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.member_group.exception.GroupMemberCountLimitException;
import site.timecapsulearchive.core.domain.member_group.exception.GroupQuitException;
import site.timecapsulearchive.core.domain.member_group.exception.MemberGroupKickDuplicatedIdException;
import site.timecapsulearchive.core.domain.member_group.exception.MemberGroupNotFoundException;
import site.timecapsulearchive.core.domain.member_group.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.member_group.facade.MemberGroupFacade;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

class MemberGroupCommandServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupInviteRepository groupInviteRepository = mock(GroupInviteRepository.class);
    private final SocialNotificationManager socialNotificationManager = mock(
        SocialNotificationManager.class);
    private final TransactionTemplate transactionTemplate = TestTransactionTemplate.spied();

    private final MemberGroupCommandService groupMemberCommandService = new MemberGroupCommandService(
        memberRepository,
        groupRepository,
        memberGroupRepository,
        groupInviteRepository,
        transactionTemplate,
        socialNotificationManager
    );

    private final MemberGroupFacade groupMemberFacade = new MemberGroupFacade(
        groupMemberCommandService,
        socialNotificationManager
    );


    @Test
    void 그룹장이_그룹원에게_그룹초대를_하면_그룹초대_알림이_요청된다() {
        //given
        Long memberId = 1L;
        SendGroupRequest request = MemberGroupDtoFixture.sendGroupRequest(1L, List.of(2L));
        GroupOwnerSummaryDto groupOwnerSummaryDto = GroupDtoFixture.groupOwnerSummaryDto(true);

        given(memberGroupRepository.findGroupMembersCount(request.groupId())).willReturn(
            Optional.of(10L));
        given(memberGroupRepository.findOwnerInMemberGroup(request.groupId(), memberId)).willReturn(
            Optional.of(groupOwnerSummaryDto));

        //when
        groupMemberCommandService.inviteGroup(memberId, request);

        //then
        verify(socialNotificationManager, times(1)).sendGroupInviteMessage(anyString(), anyString(),
            anyList());
    }

    @Test
    void 그룹장이_기존_그룹원을_포함하여_최대_수를_초과하여_그룹초대_요청을_하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        SendGroupRequest request = MemberGroupDtoFixture.sendGroupRequest(1L, List.of(2L));

        given(memberGroupRepository.findGroupMembersCount(request.groupId())).willReturn(
            Optional.of(40L));

        //when
        assertThatThrownBy(() -> groupMemberCommandService.inviteGroup(memberId, request))
            .isInstanceOf(GroupMemberCountLimitException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_COUNT_LIMIT_ERROR.getMessage());

    }

    @Test
    void 그룹장이_그룹초대를_할_때_존재하지_않은_그룹_아이디_이면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        SendGroupRequest request = MemberGroupDtoFixture.sendGroupRequest(1L, List.of(2L));

        given(memberGroupRepository.findGroupMembersCount(request.groupId())).willReturn(
            Optional.of(10L));
        given(memberGroupRepository.findOwnerInMemberGroup(request.groupId(), memberId)).willReturn(
            Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupMemberCommandService.inviteGroup(memberId, request))
            .isInstanceOf(GroupNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹장이_아닌_사용자가_그룹원에게_그룹초대를_하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        SendGroupRequest request = MemberGroupDtoFixture.sendGroupRequest(1L, List.of(2L));
        GroupOwnerSummaryDto groupOwnerSummaryDto = GroupDtoFixture.groupOwnerSummaryDto(false);

        given(memberGroupRepository.findGroupMembersCount(request.groupId())).willReturn(
            Optional.of(10L));
        given(memberGroupRepository.findOwnerInMemberGroup(request.groupId(),
            memberId)).willReturn(Optional.of(groupOwnerSummaryDto));

        //when
        //then
        assertThatThrownBy(() -> groupMemberCommandService.inviteGroup(memberId, request))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹원은_그룹초대_삭제에서_1을_반환하면_거부할_수_있다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;

        given(groupInviteRepository.findGroupInviteByGroupIdAndGroupMemberId(
            groupId, memberId)).willReturn(GroupInviteFixture.groupInvite(memberId));

        //when
        // then
        assertThatCode(() -> groupMemberCommandService.rejectRequestGroup(memberId, groupId))
            .doesNotThrowAnyException();
    }

    @Test
    void 그룹원은_그룹초대_삭제에서_0을_반환하면_거부가_실패_한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;

        given(groupInviteRepository.findGroupInviteByGroupIdAndGroupMemberId(
            groupId, memberId)).willReturn(Optional.empty());

        //when
        // then
        assertThatThrownBy(
            () -> groupMemberCommandService.rejectRequestGroup(memberId, groupId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹원은_그룹초대를_수락하면_그룹장에게_알림이_전송된다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Member groupMember = MemberFixture.member(1);

        given(groupRepository.getTotalGroupMemberCount(groupId)).willReturn(Optional.of(10L));
        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupMember));
        given(groupRepository.findGroupById(groupId)).willReturn(
            Optional.of(GroupFixture.group()));
        given(memberGroupRepository.findGroupOwnerId(groupId)).willReturn(Optional.of(2L));

        given(groupInviteRepository.findGroupInviteByGroupIdAndGroupMemberId(groupId, memberId))
            .willReturn(GroupInviteFixture.groupInvite(memberId));

        //when
        groupMemberFacade.acceptGroupInvite(memberId, groupId);

        //then
        verify(socialNotificationManager, times(1)).sendGroupAcceptMessage(anyString(), anyLong());
    }

    @Test
    void 그룹원은_그룹초대를_수락하면_알림을_보내기_위해_그룹원_이름과_그룹장_아이디를_반환한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Member groupMember = MemberFixture.member(1);
        Long groupOwnerId = 2L;

        given(groupRepository.getTotalGroupMemberCount(groupId)).willReturn(Optional.of(10L));
        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupMember));
        given(groupRepository.findGroupById(groupId)).willReturn(
            Optional.of(GroupFixture.group()));
        given(memberGroupRepository.findGroupOwnerId(groupId)).willReturn(
            Optional.of(groupOwnerId));

        given(groupInviteRepository.findGroupInviteByGroupIdAndGroupMemberId(groupId, memberId))
            .willReturn(GroupInviteFixture.groupInvite(memberId));

        //when
        GroupAcceptNotificationDto groupAcceptNotificationDto = groupMemberCommandService.acceptGroupInvite(
            memberId, groupId);

        //then
        SoftAssertions.assertSoftly(
            softly -> {
                assertThat(groupAcceptNotificationDto.groupMemberNickname()).isEqualTo(
                    groupMember.getNickname());
                assertThat(groupAcceptNotificationDto.groupOwnerId()).isEqualTo(groupOwnerId);
            }
        );
    }

    @Test
    void 그룹원은_그룹초대를_수락할_때_그룹초대가_존재하지_않으면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;
        Member groupMember = MemberFixture.member(1);

        given(groupRepository.getTotalGroupMemberCount(groupId)).willReturn(Optional.of(10L));
        given(memberRepository.findMemberById(memberId)).willReturn(Optional.of(groupMember));
        given(groupRepository.findGroupById(groupId)).willReturn(
            Optional.of(GroupFixture.group()));
        given(memberGroupRepository.findGroupOwnerId(groupId)).willReturn(Optional.of(2L));

        given(groupInviteRepository.findGroupInviteByGroupIdAndGroupMemberId(
            groupId, memberId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupMemberCommandService.acceptGroupInvite(memberId, groupId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹원은_그룹초대를_수락할_때_그룹초대_인원이_이미_최대_인원이면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long groupId = 1L;

        given(groupRepository.getTotalGroupMemberCount(groupId)).willReturn(Optional.of(30L));
        given(memberGroupRepository.findGroupOwnerId(groupId)).willReturn(Optional.of(2L));
        given(groupInviteRepository.findGroupInviteByGroupIdAndGroupMemberId(groupId, memberId))
            .willReturn(GroupInviteFixture.groupInvite(memberId));

        assertThatThrownBy(
            () -> groupMemberCommandService.acceptGroupInvite(memberId, groupId))
            .isInstanceOf(GroupMemberCountLimitException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_COUNT_LIMIT_ERROR.getMessage());
    }


    @Test
    void 그룹장인_사용자가_그룹_탈퇴를_시도하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        given(memberGroupRepository.findMemberGroupByMemberIdAndGroupId(groupOwnerId,
            groupId)).willReturn(MemberGroupFixture.owner());

        //when
        //then
        assertThatThrownBy(() -> groupMemberCommandService.quitGroup(groupOwnerId, groupId))
            .isInstanceOf(GroupQuitException.class)
            .hasMessageContaining(ErrorCode.GROUP_OWNER_QUIT_ERROR.getMessage());
    }

    @Test
    void 그룹장이_아닌_사용자가_그룹_탈퇴를_시도하면_그룹을_탈퇴한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        given(memberGroupRepository.findMemberGroupByMemberIdAndGroupId(groupOwnerId,
            groupId)).willReturn(MemberGroupFixture.notOwner());

        //when
        groupMemberCommandService.quitGroup(groupOwnerId, groupId);

        //then
        verify(memberGroupRepository, times(1)).delete(any(MemberGroup.class));
    }

    @Test
    void 그룹장인_사람이_그룹_초대를_삭제하면_삭제된다() {
        //given
        Long groupOwnerId = 1L;
        Long groupMemberId = 2L;
        Long groupInviteId = 1L;
        given(groupInviteRepository.findGroupInviteByIdAndGroupOwnerId(anyLong(),
            anyLong()))
            .willReturn(GroupInviteFixture.groupInvite(GroupFixture.group(),
                MemberFixture.memberWithMemberId(groupOwnerId),
                MemberFixture.memberWithMemberId(groupMemberId))
            );

        //when
        groupMemberCommandService.deleteGroupInvite(groupOwnerId, groupInviteId);

        //then
        verify(groupInviteRepository, times(1)).delete(any(GroupInvite.class));
    }

    @Test
    void 그룹_초대가_존재하지_않는_경우_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupInviteId = 1L;
        given(groupInviteRepository.findGroupInviteByIdAndGroupOwnerId(anyLong(),
            anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupMemberCommandService.deleteGroupInvite(groupOwnerId, groupInviteId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
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
            .isInstanceOf(MemberGroupKickDuplicatedIdException.class)
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
            () -> groupMemberCommandService.kickGroupMember(notExistGroupOwnerId, groupId,
                groupMemberId))
            .isInstanceOf(MemberGroupNotFoundException.class)
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
            () -> groupMemberCommandService.kickGroupMember(notGroupOwnerId, groupId,
                groupMemberId))
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
            () -> groupMemberCommandService.kickGroupMember(groupOwnerId, groupId,
                notExistGroupMemberId))
            .isInstanceOf(MemberGroupNotFoundException.class)
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
            .willReturn(MemberGroupFixture.notOwner());

        //when
        groupMemberCommandService.kickGroupMember(groupOwnerId, groupId, groupMemberId);

        //then
        verify(memberGroupRepository, times(1)).delete(any(MemberGroup.class));
    }
}
