package site.timecapsulearchive.core.domain.group.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
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
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.dto.GroupDtoFixture;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupOwnerSummaryDto;
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

class GroupWriteServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final GroupInviteRepository groupInviteRepository = mock(GroupInviteRepository.class);
    private final SocialNotificationManager socialNotificationManager = mock(
        SocialNotificationManager.class);
    private final TransactionTemplate transactionTemplate = TestTransactionTemplate.spied();

    private final GroupWriteService groupWriteService = new GroupWriteServiceImpl(
        memberRepository,
        groupRepository,
        memberGroupRepository,
        groupInviteRepository,
        transactionTemplate,
        socialNotificationManager
    );

    @Test
    void 그룹장이_그룹원들을_포함하여_그룹을_생성하면_그룹원들에게_그룹초대_알림이_요청된다() {
        //given
        Long memberId = 1L;
        List<Long> targetIds = List.of(2L, 3L, 4L, 5L);
        GroupCreateDto dto = GroupDtoFixture.groupCreateDto(targetIds);
        given(memberRepository.findMemberById(memberId)).willReturn(
            Optional.ofNullable(MemberFixture.member(1)));

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
        given(memberGroupRepository.findOwnerInMemberGroup(groupId, memberId)).willReturn(
            Optional.of(groupOwnerSummaryDto));

        //when
        //then
        assertThatThrownBy(() -> groupWriteService.inviteGroup(memberId, groupId, targetId))
            .isInstanceOf(GroupOwnerAuthenticateException.class)
            .hasMessageContaining(ErrorCode.GROUP_OWNER_AUTHENTICATE_ERROR.getMessage());
    }


}

