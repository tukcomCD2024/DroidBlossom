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
import site.timecapsulearchive.core.domain.group.data.dto.GroupUpdateDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.exception.GroupDeleteFailException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group.service.command.GroupCommandService;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member_group.exception.MemberGroupNotFoundException;
import site.timecapsulearchive.core.domain.member_group.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;
import site.timecapsulearchive.core.infra.s3.manager.S3ObjectManager;

class GroupCommandServiceTest {

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

    private final GroupCommandService groupCommandService = new GroupCommandService(
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
        groupCommandService.createGroup(memberId, dto);

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
        assertThatThrownBy(() -> groupCommandService.createGroup(memberId, dto))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessageContaining(ErrorCode.MEMBER_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 존재하지_않는_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long notExistGroupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupCommandService.deleteGroup(memberId, notExistGroupId))
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
            MemberGroupFixture.memberGroupsWithoutOwner());

        //when
        //then
        assertThatThrownBy(() -> groupCommandService.deleteGroup(groupMemberId, groupId))
            .isExactlyInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹원이_존재하는_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupMemberExistGroupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(group());
        given(memberGroupRepository.findMemberGroupsByGroupId(groupMemberExistGroupId)).willReturn(
            MemberGroupFixture.memberGroupsWithOwner());

        //when
        //then
        assertThatThrownBy(
            () -> groupCommandService.deleteGroup(groupOwnerId, groupMemberExistGroupId))
            .isExactlyInstanceOf(GroupDeleteFailException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_EXIST_ERROR.getMessage());
    }

    private Optional<Group> group() {
        return Optional.of(
            GroupFixture.group()
        );
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
            () -> groupCommandService.deleteGroup(groupOwnerId, groupCapsuleExistGroupId))
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
            () -> groupCommandService.deleteGroup(groupOwnerId,
                groupId)).doesNotThrowAnyException();
        verify(groupRepository, times(1)).delete(any(Group.class));
    }

    @Test
    void 그룹원이_아닌_경우_그룹_정보를_업데이트하면_예외가_발생한다() {
        //given
        Long notGroupMemberId = 1L;
        Long groupId = 1L;
        GroupUpdateDto groupUpdateDto = getGroupUpdateDto();
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupCommandService.updateGroup(notGroupMemberId, groupId, groupUpdateDto))
            .isInstanceOf(MemberGroupNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_MEMBER_NOT_FOUND_ERROR.getMessage());
    }

    private GroupUpdateDto getGroupUpdateDto() {
        return GroupUpdateDto.builder()
            .groupName("updated_name")
            .groupDescription("updated_description")
            .groupImageDirectory("updated_image_directory")
            .groupImageProfileFileName("updated_group_image_profile_file_name")
            .build();
    }

    @Test
    void 그룹장이_아닌_경우_그룹_정보를_업데이트하면_예외가_발생한다() {
        //given
        Long notGroupOwnerId = 1L;
        Long groupId = 1L;
        GroupUpdateDto groupUpdateDto = getGroupUpdateDto();
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.of(Boolean.FALSE));

        //when
        //then
        assertThatThrownBy(
            () -> groupCommandService.updateGroup(notGroupOwnerId, groupId, groupUpdateDto))
            .isInstanceOf(NoGroupAuthorityException.class)
            .hasMessageContaining(ErrorCode.NO_GROUP_AUTHORITY_ERROR.getMessage());
    }

    @Test
    void 그룹이_없는_경우_그룹_정보를_업데이트하면_예외가_발생한다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        GroupUpdateDto groupUpdateDto = getGroupUpdateDto();
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.of(Boolean.TRUE));
        given(groupRepository.findGroupById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(
            () -> groupCommandService.updateGroup(groupOwnerId, groupId, groupUpdateDto))
            .isInstanceOf(GroupNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹장이_그룹_정보를_업데이트하면_그룹_정보가_업데이트된다() {
        //given
        Long groupOwnerId = 1L;
        Long groupId = 1L;
        GroupUpdateDto groupUpdateDto = getGroupUpdateDto();
        given(memberGroupRepository.findIsOwnerByMemberIdAndGroupId(anyLong(), anyLong()))
            .willReturn(Optional.of(Boolean.TRUE));
        given(groupRepository.findGroupById(anyLong())).willReturn(group());

        //when
        //then
        assertThatCode(() -> groupCommandService.updateGroup(groupOwnerId, groupId,
            groupUpdateDto)).doesNotThrowAnyException();
    }
}

