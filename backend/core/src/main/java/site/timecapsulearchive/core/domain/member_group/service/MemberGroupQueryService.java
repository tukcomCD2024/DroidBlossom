package site.timecapsulearchive.core.domain.member_group.service;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;
import site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository.GroupInviteRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberGroupQueryService {

    private final GroupInviteRepository groupInviteRepository;

    public Slice<GroupInviteSummaryDto> findGroupReceivingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupInviteRepository.findGroupReceivingInvitesSlice(memberId, size, createdAt);
    }

    public List<GroupSendingInviteMemberDto> findGroupSendingInvites(
        final Long memberId,
        final Long groupId
    ) {
        return groupInviteRepository.findGroupSendingInvites(memberId, groupId);
    }
}
