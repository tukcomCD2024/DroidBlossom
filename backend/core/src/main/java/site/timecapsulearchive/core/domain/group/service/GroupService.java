package site.timecapsulearchive.core.domain.group.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupInviteMessageDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;
import site.timecapsulearchive.core.domain.group.entity.MemberGroup;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.group.repository.GroupQueryRepository;
import site.timecapsulearchive.core.domain.group.repository.GroupRepository;
import site.timecapsulearchive.core.domain.group.repository.MemberGroupRepository;
import site.timecapsulearchive.core.domain.member.entity.Member;
import site.timecapsulearchive.core.domain.member.exception.MemberNotFoundException;
import site.timecapsulearchive.core.domain.member.repository.MemberRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final TransactionTemplate transactionTemplate;
    private final GroupInviteMessageManager groupInviteMessageManager;
    private final GroupQueryRepository groupQueryRepository;

    @Transactional
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

    public Group findGroupById(Long groupId) {
        return groupRepository.findGroupById(groupId)
            .orElseThrow(GroupNotFoundException::new);
    }

    public Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupQueryRepository.findGroupsSlice(memberId, size, createdAt);
    }

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
}
