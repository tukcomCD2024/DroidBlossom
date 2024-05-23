package site.timecapsulearchive.core.domain.group.repository;

import java.time.ZonedDateTime;
import java.util.List;
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

    Optional<GroupDetailDto> findGroupDetailByGroupIdAndMemberId(final Long groupId, final Long memberId);

    Long findGroupCapsuleCount(final Long groupId);

    Boolean findGroupEditPermission(final Long groupId, final Long memberId);

    List<Long> findFriendIds(final List<Long> groupMemberIds, final Long memberId);
}
