package site.timecapsulearchive.core.domain.group.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.common.fixture.domain.GroupFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberFixture;
import site.timecapsulearchive.core.common.fixture.domain.MemberGroupFixture;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupDeleteFailException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupQueryRepository;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;
import site.timecapsulearchive.core.infra.s3.manager.S3ObjectManager;

/**
 * 테스트 케이스
 * <ol>
 * <li>모든 분기 테스트</li>
 * <ul>
 *    <li>그룹이 없는 경우</li>
 *    <li>그룹장이 아닌 경우</li>
 *    <li>그룹원이 있는 경우</li>
 *    <li>그룹 캡슐이 존재하는 경우</li>
 *    <li>그룹을 삭제할 수 있는 모든 조건(그룹 존재, 그룹에 속한 그룹원 없음, 요청한 사용자가 그룹장, 그룹 캡슐 없음)을 만족한 경우</li>
 * </ul>
 * </ol>
 */
class GroupServiceTest {

    private final GroupRepository groupRepository = mock(GroupRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberGroupRepository memberGroupRepository = mock(MemberGroupRepository.class);
    private final TransactionTemplate transactionTemplate = mock(TransactionTemplate.class);
    private final SocialNotificationManager socialNotificationManager = mock(
        SocialNotificationManager.class);
    private final GroupQueryRepository groupQueryRepository = mock(GroupQueryRepository.class);
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository = mock(
        GroupCapsuleQueryRepository.class);
    private final S3ObjectManager s3ObjectManager = mock(S3ObjectManager.class);

    private final GroupService groupService = new GroupService(
        groupRepository,
        memberRepository,
        memberGroupRepository,
        transactionTemplate,
        socialNotificationManager,
        groupQueryRepository,
        groupCapsuleQueryRepository,
        s3ObjectManager
    );

    @Test
    void 존재하지_않는_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long memberId = 1L;
        Long notExistGroupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> groupService.deleteGroup(memberId, notExistGroupId))
            .isExactlyInstanceOf(GroupNotFoundException.class)
            .hasMessageContaining(ErrorCode.GROUP_NOT_FOUND_ERROR.getMessage());
    }

    @Test
    void 그룹장이_아닌_사용자가_그룹_아이디로_삭제를_시도하면_예외가_발생한다() {
        //given
        Long groupMemberId = 1L;
        Long groupId = 1L;

        given(groupRepository.findGroupById(anyLong())).willReturn(group());
        given(memberGroupRepository.findMemberGroupsByGroupId(groupId)).willReturn(notOwnerGroupMember());

        //when
        //then
        assertThatThrownBy(() -> groupService.deleteGroup(groupMemberId, groupId))
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
        assertThatThrownBy(() -> groupService.deleteGroup(groupOwnerId, groupMemberExistGroupId))
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
        MemberGroup groupOwner = MemberGroupFixture.groupOwner(MemberFixture.memberWithMemberId(1L), group);

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
        given(groupCapsuleQueryRepository.findGroupCapsuleExistByGroupId(anyLong())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> groupService.deleteGroup(groupOwnerId, groupCapsuleExistGroupId))
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
        given(groupCapsuleQueryRepository.findGroupCapsuleExistByGroupId(anyLong())).willReturn(false);

        //when
        //then
        assertThatCode(() -> groupService.deleteGroup(groupOwnerId, groupId)).doesNotThrowAnyException();
        verify(groupRepository, times(1)).delete(any(Group.class));
    }
}