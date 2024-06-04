package site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;

public interface GroupInviteQueryRepository {

    void bulkSave(final Long groupOwnerId, final Long groupId, final List<Long> groupMemberIds);

    List<Long> findGroupInviteIdsByGroupIdAndGroupOwnerId(final Long groupId, final Long memberId);

    Slice<GroupInviteSummaryDto> findGroupInvitesSummary(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    List<Long> getGroupMemberIdsByGroupIdAndGroupOwnerId(final Long groupId, final Long memberId);
}
