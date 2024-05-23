package site.timecapsulearchive.core.domain.member_group.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository.GroupInviteRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberGroupQueryService {

    private final GroupInviteRepository groupInviteRepository;

    public Slice<GroupInviteSummaryDto> findGroupInvites(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return groupInviteRepository.findGroupInvitesSummary(memberId, size, createdAt);
    }

}
