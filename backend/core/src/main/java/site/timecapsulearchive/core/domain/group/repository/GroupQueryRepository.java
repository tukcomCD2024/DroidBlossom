package site.timecapsulearchive.core.domain.group.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;


public interface GroupQueryRepository {

    Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    Optional<GroupDetailDto> findGroupDetailByGroupId(final Long groupId);

}
