package site.timecapsulearchive.core.domain.member_group.service;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.exception.GroupNotFoundException;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInvitesSliceRequestDto;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteRepository;
import site.timecapsulearchive.core.domain.member_group.repository.member_group_repository.MemberGroupRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberGroupQueryService {

    private final GroupInviteRepository groupInviteRepository;
    private final MemberGroupRepository memberGroupRepository;

    public Slice<GroupInviteSummaryDto> findGroupReceivingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupInviteRepository.findGroupReceivingInvitesSlice(memberId, size, createdAt);
    }

    public List<Long> findGroupMemberIds(final Long groupId) {
        return memberGroupRepository.findGroupMemberIds(groupId).orElseThrow(
            GroupNotFoundException::new);
    }

    public List<GroupMemberDto> findGroupMemberInfos(
        final Long memberId,
        final Long groupId
    ) {
        return memberGroupRepository.findGroupMemberInfos(memberId, groupId);
    }

    public Slice<GroupSendingInviteMemberDto> findGroupSendingInvites(final
    GroupSendingInvitesSliceRequestDto dto) {
        return groupInviteRepository.findGroupSendingInvites(dto);
    }
}
