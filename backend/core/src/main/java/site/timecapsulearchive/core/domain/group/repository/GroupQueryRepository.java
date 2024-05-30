package site.timecapsulearchive.core.domain.group.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.group.data.dto.FinalGroupSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;


public interface GroupQueryRepository {

    Slice<FinalGroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    Optional<GroupDetailDto> findGroupDetailByGroupIdAndMemberId(final Long groupId,
        final Long memberId);

    Optional<Long> getTotalGroupMemberCount(Long groupId);
}
