package site.timecapsulearchive.core.domain.group.service.write;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.capsule.group_capsule.repository.GroupCapsuleQueryRepository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupDeleteFailException;
import site.timecapsulearchive.core.domain.group.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupOwnerAuthenticateException;
import site.timecapsulearchive.core.domain.group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.group.repository.groupRepository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;
import site.timecapsulearchive.core.infra.s3.manager.S3ObjectManager;

@Service
@RequiredArgsConstructor
public class GroupWriteServiceImpl implements GroupWriteService {

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
                groupInviteRepository.bulkSave(memberId, dto.targetIds());
            }
        });

        socialNotificationManager.sendGroupInviteMessage(member.getNickname(),
            dto.groupProfileUrl(), dto.targetIds());
    }

    public void inviteGroup(final Long memberId, final Long groupId, final Long targetId) {
        final Member groupOwner = memberRepository.findMemberById(memberId).orElseThrow(
            MemberNotFoundException::new);

        final Member groupMember = memberRepository.findMemberById(targetId).orElseThrow(
            MemberNotFoundException::new);

        final Group group = groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);

        final GroupInvite groupInvite = GroupInvite.createOf(group, groupOwner, groupMember);

        final GroupOwnerSummaryDto[] summaryDto = new GroupOwnerSummaryDto[1];

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                summaryDto[0] = memberGroupRepository.findOwnerInMemberGroup(
                    groupId, memberId).orElseThrow(GroupNotFoundException::new);

                if (!summaryDto[0].isOwner()) {
                    throw new GroupOwnerAuthenticateException();
                }

                groupInviteRepository.save(groupInvite);
            }
        });

        socialNotificationManager.sendGroupInviteMessage(summaryDto[0].nickname(),
            summaryDto[0].groupProfileUrl(), List.of(targetId));
    }

    @Transactional
    public void rejectRequestGroup(final Long memberId, final Long groupId, final Long targetId) {
        final int isDenyRequest = groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
            groupId, targetId, memberId);

        if (isDenyRequest != 1) {
            throw new GroupInviteNotFoundException();
        }
    }

    public void acceptGroupInvite(final Long memberId, final Long groupId, final Long targetId) {
        final Member groupMember = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        final Group group = groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                final int isDenyRequest = groupInviteRepository.deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
                    groupId, targetId, memberId);

                if (isDenyRequest != 1) {
                    throw new GroupInviteNotFoundException();
                }

                memberGroupRepository.save(MemberGroup.createGroupMember(groupMember, group));
            }
        });

        socialNotificationManager.sendGroupAcceptMessage(groupMember.getNickname(), targetId);
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
        final String[] groupProfilePath = new String[1];

        transactionTemplate.executeWithoutResult(transactionStatus -> {
            final Group group = groupRepository.findGroupById(groupId)
                .orElseThrow(GroupNotFoundException::new);

            final List<MemberGroup> groupMembers = memberGroupRepository.findMemberGroupsByGroupId(
                groupId);

            checkGroupOwnership(memberId, groupMembers);
            checkGroupMemberExist(groupMembers);
            checkGroupCapsuleExist(groupId);

            List<Long> groupInviteIds = groupInviteRepository.findGroupInviteIdsByGroupIdAndGroupOwnerId(
                groupId, memberId);
            groupInviteRepository.bulkDelete(groupInviteIds);

            groupRepository.delete(group);
            groupProfilePath[0] = group.getGroupProfileUrl();
        });

        s3ObjectManager.deleteObject(groupProfilePath[0]);
    }

    private void checkGroupOwnership(Long memberId, List<MemberGroup> groupMembers) {
        final boolean isGroupOwner = groupMembers.stream()
            .anyMatch(mg -> mg.getMember().getId().equals(memberId) && mg.getIsOwner());
        if (!isGroupOwner) {
            throw new GroupDeleteFailException(ErrorCode.NO_GROUP_AUTHORITY_ERROR);
        }
    }

    private void checkGroupMemberExist(List<MemberGroup> groupMembers) {
        final boolean groupMemberExist = groupMembers.size() > 1;
        if (groupMemberExist) {
            throw new GroupDeleteFailException(ErrorCode.GROUP_MEMBER_EXIST_ERROR);
        }
        groupMembers.forEach(memberGroupRepository::delete);
    }

    private void checkGroupCapsuleExist(Long groupId) {
        final boolean groupCapsuleExist = groupCapsuleQueryRepository.findGroupCapsuleExistByGroupId(
            groupId);
        if (groupCapsuleExist) {
            throw new GroupDeleteFailException(ErrorCode.GROUP_CAPSULE_EXIST_ERROR);
        }
    }
}
