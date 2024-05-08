package site.timecapsulearchive.core.domain.group.service;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;
import site.timecapsulearchive.core.domain.group.entity.Group;

public interface GroupReadService {

    Group findGroupById(final Long groupId);

    Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    GroupDetailDto findGroupDetailByGroupId(final Long memberId, final Long groupId);
}
