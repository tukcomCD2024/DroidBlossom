package site.timecapsulearchive.core.domain.group.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;


public interface GroupQueryRepository {

    Slice<GroupSummaryDto> findGroupSummaries(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    List<String> getGroupOwnerProfileUrls(final List<Long> groupIds);

    List<Long> getTotalGroupMemberCount(final List<Long> groupIds);

    Optional<GroupDetailDto> findGroupDetailByGroupIdAndMemberId(final Long groupId,
        final Long memberId);

    Optional<Long> getTotalGroupMemberCount(Long groupId);
}
