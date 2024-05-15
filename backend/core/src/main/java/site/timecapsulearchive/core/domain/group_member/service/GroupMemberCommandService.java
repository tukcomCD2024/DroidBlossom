package site.timecapsulearchive.core.domain.group_member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group_member.data.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group_member.entity.GroupInvite;
import site.timecapsulearchive.core.domain.group_member.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group_member.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.group_member.exception.GroupMemberNotFoundException;
import site.timecapsulearchive.core.domain.group_member.exception.GroupQuitException;
import site.timecapsulearchive.core.domain.group_member.exception.NoGroupAuthorityException;
import site.timecapsulearchive.core.domain.group_member.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.group_member.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Service
@RequiredArgsConstructor
public class GroupMemberCommandService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInviteRepository groupInviteRepository;
    private final TransactionTemplate transactionTemplate;
    private final SocialNotificationManager socialNotificationManager;

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
                    throw new NoGroupAuthorityException();
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
     * 사용자는 사용자가 속한 그룹을 탈퇴한다.
     * <br><u><b>주의</b></u> - 그룹 탈퇴 시 아래 조건에 해당하면 예외가 발생한다.
     * <br>1. 그룹원이 그룹장인 경우
     *
     * @param memberId 그룹에서 탈퇴할 사용자
     * @param groupId  탈퇴할 그룹 아이디
     */
    @Transactional
    public void quitGroup(final Long memberId, final Long groupId) {
        final MemberGroup groupMember = memberGroupRepository.findMemberGroupByMemberIdAndGroupId(
                memberId, groupId)
            .orElseThrow(GroupMemberNotFoundException::new);
        if (groupMember.getIsOwner()) {
            throw new GroupQuitException(ErrorCode.GROUP_OWNER_QUIT_ERROR);
        }

        memberGroupRepository.delete(groupMember);
    }
}
