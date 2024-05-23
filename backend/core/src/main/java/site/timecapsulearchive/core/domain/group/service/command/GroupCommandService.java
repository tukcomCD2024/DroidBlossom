package site.timecapsulearchive.core.domain.group.service.command;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupUpdateDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.exception.GroupDeleteFailException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.domain.member_group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.member_group.exception.MemberGroupNotFoundException;
import site.timecapsulearchive.core.domain.member_group.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;
import site.timecapsulearchive.core.infra.s3.manager.S3ObjectManager;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

@Service
@RequiredArgsConstructor
public class GroupCommandService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInviteRepository groupInviteRepository;
    private final TransactionTemplate transactionTemplate;
    private final SocialNotificationManager socialNotificationManager;
    private final GroupCapsuleQueryRepository groupCapsuleQueryRepository;
    private final S3ObjectManager s3ObjectManager;

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
                groupInviteRepository.bulkSave(memberId, group.getId(), dto.targetIds());
            }
        });

        socialNotificationManager.sendGroupInviteMessage(member.getNickname(),
            dto.groupProfileUrl(), dto.targetIds());
    }

    /**
     * 사용자가 그룹장인 그룹을 삭제한다.
     * <br><u><b>주의</b></u> - 그룹 삭제 시 아래 조건에 해당하면 예외가 발생한다.
     * <br>1. 그룹이 존재하지 않는 경우
     * <br>2. 그룹 삭제를 요청한 사용자가 그룹장이 아닌 경우
     * <br>3. 그룹에 멤버가 존재하는 경우
     * <br>4. 그룹에 그룹 캡슐이 남아있는 경우
     *
     * @param memberId 그룹에 속한 그룹장 아이디
     * @param groupId  그룹 아이디
     */
    public void deleteGroup(final Long memberId, final Long groupId) {
        final String groupProfilePath = transactionTemplate.execute(ignored -> {
            final Group group = groupRepository.findGroupById(groupId)
                .orElseThrow(GroupNotFoundException::new);

            final List<MemberGroup> groupMembers = memberGroupRepository.findMemberGroupsByGroupId(
                groupId);

            checkGroupOwnership(memberId, groupMembers);
            checkGroupMemberExist(groupMembers);
            checkGroupCapsuleExist(groupId);

            final List<Long> groupInviteIds = groupInviteRepository.findGroupInviteIdsByGroupIdAndGroupOwnerId(
                groupId, memberId);
            groupInviteRepository.bulkDelete(groupInviteIds);

            groupRepository.delete(group);
            return group.getGroupProfileUrl();
        });

        s3ObjectManager.deleteObject(groupProfilePath);
    }

    private void checkGroupOwnership(final Long memberId, final List<MemberGroup> groupMembers) {
        final boolean isGroupOwner = groupMembers.stream()
            .anyMatch(mg -> mg.getMember().getId().equals(memberId) && mg.getIsOwner());
        if (!isGroupOwner) {
            throw new NoGroupAuthorityException();
        }
    }

    private void checkGroupMemberExist(final List<MemberGroup> groupMembers) {
        final boolean groupMemberExist = groupMembers.size() > 1;
        if (groupMemberExist) {
            throw new GroupDeleteFailException(ErrorCode.GROUP_MEMBER_EXIST_ERROR);
        }
        groupMembers.forEach(memberGroupRepository::delete);
    }

    private void checkGroupCapsuleExist(final Long groupId) {
        final boolean groupCapsuleExist = groupCapsuleQueryRepository.findGroupCapsuleExistByGroupId(
            groupId);
        if (groupCapsuleExist) {
            throw new GroupDeleteFailException(ErrorCode.GROUP_CAPSULE_EXIST_ERROR);
        }
    }

    /**
     * 그룹 정보를 업데이트한다.
     * <br><u><b>주의</b></u> - 그룹 정보 변경 시 아래 조건에 해당하면 예외가 발생한다.
     * <br>1. 해당 그룹원을 찾을 수 없는 경우
     * <br>2. 그룹장이 아닌 경우
     * <br>3. 그룹을 찾을 수 없는 경우
     *
     * @param groupOwnerId 그룹장 아이디
     * @param groupId      그룹 아이디
     * @param dto          업데이트할 그룹 정보
     */
    @Transactional
    public void updateGroup(final Long groupOwnerId, final Long groupId, final GroupUpdateDto dto) {
        checkGroupOwnership(groupOwnerId, groupId);

        final Group group = groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);

        group.updateGroupName(dto.groupName());
        group.updateGroupDescription(dto.groupDescription());

        final String groupProfileUrl = S3UrlGenerator.generateFileName(
            groupOwnerId,
            dto.groupImageDirectory(),
            dto.groupImageProfileFileName()
        );
        group.updateGroupProfileUrl(groupProfileUrl);
    }

    private void checkGroupOwnership(final Long groupOwnerId, final Long groupId) {
        final Boolean isOwner = memberGroupRepository.findIsOwnerByMemberIdAndGroupId(
                groupOwnerId,
                groupId)
            .orElseThrow(MemberGroupNotFoundException::new);
        if (!isOwner) {
            throw new NoGroupAuthorityException();
        }
    }
}
