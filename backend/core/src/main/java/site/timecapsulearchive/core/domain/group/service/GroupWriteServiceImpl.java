package site.timecapsulearchive.core.domain.group.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupOwnerSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.GroupInvite;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.exception.GroupOwnerAuthenticateException;
import site.timecapsulearchive.core.domain.group.repository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.MemberGroupRepository;
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

    @Override
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

}
