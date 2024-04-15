package site.timecapsulearchive.core.domain.group.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupInviteMessageDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final TransactionTemplate transactionTemplate;
    private final GroupInviteMessageManager groupInviteMessageManager;

    public GroupService(GroupRepository groupRepository,
        MemberRepository memberRepository,
        MemberGroupRepository memberGroupRepository,
        PlatformTransactionManager platformTransactionManager,
        GroupInviteMessageManager groupInviteMessageManager) {
        this.groupRepository = groupRepository;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.groupInviteMessageManager = groupInviteMessageManager;
        this.memberRepository = memberRepository;
        this.memberGroupRepository = memberGroupRepository;
    }

    public void createGroup(final Long memberId, final GroupCreateDto dto) {
        Member member = memberRepository.findMemberById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        Group group = dto.toEntity();

        MemberGroup memberGroup = MemberGroup.createGroupOwner(member, group);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                groupRepository.save(group);
                memberGroupRepository.save(memberGroup);
            }
        });

        GroupInviteMessageDto groupInviteMessageDto = dto.toInviteMessageDto(member.getNickname());
        groupInviteMessageManager.sendGroupInviteMessage(groupInviteMessageDto);
    }

    @Transactional(readOnly = true)
    public Group findGroupById(Long groupId) {
        return groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
    }

}
