package site.timecapsulearchive.core.domain.group.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import java.time.ZonedDateTime;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupDeleteFailException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupQueryRepository;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final TransactionTemplate transactionTemplate;
    private final SocialNotificationManager socialNotificationManager;
    private final GroupQueryRepository groupQueryRepository;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;

    public void createGroup(final Long memberId, final GroupCreateDto dto) {
        final Member member = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        final Group group = dto.toEntity();

        final MemberGroup memberGroup = MemberGroup.createGroupOwner(member, group);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                groupRepository.save(group);
                memberGroupRepository.save(memberGroup);
            }
        });

        socialNotificationManager.sendGroupInviteMessage(member.getNickname(),
            dto.groupProfileUrl(), dto.targetIds());
    }

    @Transactional(readOnly = true)
    public Group findGroupById(Long groupId) {
        return groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupQueryRepository.findGroupsSlice(memberId, size, createdAt);
    }

    @Transactional(readOnly = true)
    public GroupDetailDto findGroupDetailByGroupId(final Long memberId, final Long groupId) {
        final GroupDetailDto groupDetailDto = groupQueryRepository.findGroupDetailByGroupId(groupId)
            .orElseThrow(GroupNotFoundException::new);

        final boolean isGroupMember = groupDetailDto.members()
            .stream()
            .anyMatch(m -> m.memberId().equals(memberId));

        if (!isGroupMember) {
            throw new GroupNotFoundException();
        }

        return groupDetailDto;
    }

    /**
     * 사용자가 그룹장인 그룹을 삭제한다.
     * <br><u><b>주의</b></u> - 그룹 삭제 시 아래 조건에 해당하면 예외가 발생한다.
     * <br>1. 그룹에 멤버가 존재하는 경우
     * <br>2. 그룹 삭제를 요청한 사용자가 그룹장이 아닌 경우
     * <br>3. 그룹에 그룹 캡슐이 남아있는 경우
     * @param memberId 그룹에 속한 그룹장 아이디
     * @param groupId 그룹 아이디
     */
    @Transactional
    public void deleteGroup(final Long memberId, final Long groupId) {
        final Group group = groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);

        final List<MemberGroup> groupMembers = memberGroupRepository.findMemberGroupsByGroupId(groupId);
        final boolean isGroupOwner = groupMembers.stream()
            .anyMatch(mg -> mg.getMember().getId().equals(memberId) && mg.getIsOwner());
        if (!isGroupOwner) {
            throw new GroupDeleteFailException(ErrorCode.NO_GROUP_AUTHORITY_ERROR);
        }

        final boolean groupMemberExist = groupMembers.size() > 1;
        if (groupMemberExist) {
            throw new GroupDeleteFailException(ErrorCode.GROUP_MEMBER_EXIST_ERROR);
        }
        groupMembers.forEach(memberGroupRepository::delete);

        final Long groupCapsuleCount = groupCapsuleQueryRepository.findGroupCapsuleCountByGroupId(groupId)
            .orElseThrow(GroupNotFoundException::new);
        final boolean groupCapsuleExist = groupCapsuleCount != 0;
        if (groupCapsuleExist) {
            throw new GroupDeleteFailException(ErrorCode.GROUP_CAPSULE_EXIST_ERROR);
        }

        groupRepository.delete(group);
    }
}
