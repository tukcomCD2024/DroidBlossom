package site.timecapsulearchive.core.domain.group.service.write;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupInviteNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupOwnerAuthenticateException;
import site.timecapsulearchive.core.domain.group.repository.groupInviteRepository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.group.repository.groupRepository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.memberGroupRepository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;
import site.timecapsulearchive.core.infra.queue.manager.SocialNotificationManager;

@Service
@RequiredArgsConstructor
public class GroupWriteServiceImpl implements GroupWriteService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final GroupInviteRepository groupInviteRepository;
    private final TransactionTemplate transactionTemplate;
    private final SocialNotificationManager socialNotificationManager;

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

        final GroupInvite groupInvite = GroupInvite.createOf(groupOwner, groupMember);

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
    public void rejectRequestGroup(final Long memberId, final Long targetId) {
        final int isDenyRequest = groupInviteRepository.deleteGroupInviteByGroupOwnerIdAndGroupMemberId(
            targetId, memberId);

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
                final int isDenyRequest = groupInviteRepository.deleteGroupInviteByGroupOwnerIdAndGroupMemberId(
                    targetId, memberId);

                if (isDenyRequest != 1) {
                    throw new GroupInviteNotFoundException();
                }

                memberGroupRepository.save(MemberGroup.createGroupMember(groupMember, group));
            }
        });

        socialNotificationManager.sendGroupAcceptMessage(groupMember.getNickname(), targetId);
    }


}
