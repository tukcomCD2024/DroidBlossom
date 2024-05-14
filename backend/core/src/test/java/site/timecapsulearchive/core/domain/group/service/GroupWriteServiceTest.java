package site.timecapsulearchive.core.domain.group.service;


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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.dependency.TestTransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.common.fixture.dto.GroupDtoFixture;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupDeleteFailException;
import site.timecapsulearchive.core.domain.group.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupOwnerAuthenticateException;
import site.timecapsulearchive.core.domain.group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.group.repository.groupRepository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.group.service.write.GroupWriteService;
import site.timecapsulearchive.core.domain.group.service.write.GroupWriteServiceImpl;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;
import site.timecapsulearchive.core.infra.s3.manager.S3ObjectManager;

class GroupWriteServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupInviteRepository groupInviteRepository = mock(GroupInviteRepository.class);
    private final SocialNotificationManager socialNotificationManager = mock(
        SocialNotificationManager.class);
    private final TransactionTemplate transactionTemplate = TestTransactionTemplate.spied();
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository = mock(
        GroupCapsuleQueryRepository.class);
    private final S3ObjectManager s3ObjectManager = mock(S3ObjectManager.class);

    private final GroupWriteService groupWriteService = new GroupWriteServiceImpl(
        memberRepository,
        groupRepository,
        memberGroupRepository,
        groupInviteRepository,
        transactionTemplate,
        socialNotificationManager,
        groupCapsuleQueryRepository,
        s3ObjectManager
    );

    @Test
    void 그룹장이_그룹원들을_포함하여_그룹을_생성하면_그룹원들에게_그룹초대_알림이_요청된다() {
        //given
        Long memberId = 1L;
        List<Long> targetIds = List.of(2L, 3L, 4L, 5L);
        GroupCreateDto dto = GroupDtoFixture.groupCreateDto(targetIds);
        given(memberRepository.findMemberById(memberId)).willReturn(
            Optional.of(MemberFixture.member(1)));

        //when
        groupWriteService.createGroup(memberId, dto);

        //then
        verify(socialNotificationManager, times(1)).sendGroupInviteMessage(
            anyString(), anyString(), anyList());
    }

    @Test
    void 그룹장이_그룹초대를_할_때_존재하지_않는_그룹장_아이디면_예외가_발생한다() {
        //given
        Long memberId = -1L;
        List<Long> targetIds = List.of(2L, 3L, 4L, 5L);
        GroupCreateDto dto = GroupDtoFixture.groupCreateDto(targetIds);
        given(memberRepository.findMemberById(memberId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupWriteService.createGroup(memberId, dto))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND_ERROR.getMessage());
    }


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
        groupWriteService.inviteGroup(memberId, groupId, targetId);

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
        assertThatThrownBy(() -> groupWriteService.inviteGroup(memberId, groupId, targetId))
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
        assertThatThrownBy(() -> groupWriteService.inviteGroup(memberId, groupId, targetId))
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
        assertThatCode(() -> groupWriteService.rejectRequestGroup(memberId, groupId, targetId))
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
        assertThatThrownBy(() -> groupWriteService.rejectRequestGroup(memberId, groupId, targetId))
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
        groupWriteService.acceptGroupInvite(memberId, groupId, targetId);

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
        assertThatThrownBy(() -> groupWriteService.acceptGroupInvite(memberId, groupId, targetId))
            .isInstanceOf(GroupInviteNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_INVITATION_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 존재하지_않는_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long notExistGroupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupWriteService.deleteGroup(memberId, notExistGroupId))
            .isExactlyInstanceOf(GroupNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹장이_아닌_사용자가_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long groupMemberId = 1L;
        Long groupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(group());
        given(memberGroupRepository.findMemberGroupsByGroupId(groupId)).willReturn(
            notOwnerGroupMember());

        //when
        //then
        assertThatThrownBy(() -> groupWriteService.deleteGroup(groupMemberId, groupId))
            .isExactlyInstanceOf(GroupDeleteFailException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    private List<MemberGroup> notOwnerGroupMember() {
        return List.of(
            MemberGroupFixture.memberGroup(
                MemberFixture.memberWithMemberId(1),
                GroupFixture.group(),
                false
            )
        );
    }

    @Test
    void 그룹원이_존재하는_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupMemberExistGroupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(group());
        given(memberGroupRepository.findMemberGroupsByGroupId(groupMemberExistGroupId)).willReturn(
            groupMembers());

        //when
        //then
        assertThatThrownBy(
            () -> groupWriteService.deleteGroup(groupOwnerId, groupMemberExistGroupId))
            .isExactlyInstanceOf(GroupDeleteFailException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_EXIST_ERROR.getMessage());
    }

    private Optional<Group> group() {
        return Optional.of(
            GroupFixture.group()
        );
    }

    private List<MemberGroup> groupMembers() {
        Group group = GroupFixture.group();
        MemberGroup groupOwner = MemberGroupFixture.groupOwner(MemberFixture.memberWithMemberId(1L),
            group);

        List<MemberGroup> memberGroups = MemberGroupFixture.memberGroups(
            MemberFixture.membersWithMemberId(2, 2),
            group
        );

        List<MemberGroup> result = new ArrayList<>(memberGroups);
        result.add(groupOwner);
        return result;
    }

    @Test
    void 그룹_캡슐이_존재하는_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupCapsuleExistGroupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(group());
        given(memberGroupRepository.findMemberGroupsByGroupId(groupCapsuleExistGroupId)).willReturn(
            ownerGroupMember());
        given(groupCapsuleQueryRepository.findGroupCapsuleExistByGroupId(anyLong())).willReturn(
            true);

        //when
        //then
        assertThatThrownBy(
            () -> groupWriteService.deleteGroup(groupOwnerId, groupCapsuleExistGroupId))
            .isExactlyInstanceOf(GroupDeleteFailException.class)
            .hasMessageContaining(ErrorCode.GROUP_CAPSULE_EXIST_ERROR.getMessage());
    }

    private List<MemberGroup> ownerGroupMember() {
        return List.of(
            MemberGroupFixture.groupOwner(
                MemberFixture.memberWithMemberId(1),
                GroupFixture.group()
            )
        );
    }

    @Test
    void 그룹을_삭제할_조건을_만족한_그룹_아이디로_삭제를_시도하면_그룹이_삭제된다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(group());
        given(memberGroupRepository.findMemberGroupsByGroupId(groupId)).willReturn(
            ownerGroupMember());
        given(groupCapsuleQueryRepository.findGroupCapsuleExistByGroupId(anyLong())).willReturn(
            false);

        //when
        //then
        assertThatCode(
            () -> groupWriteService.deleteGroup(groupOwnerId, groupId)).doesNotThrowAnyException();
        verify(groupRepository, times(1)).delete(any(Group.class));
    }
}

