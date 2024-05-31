package site.timecapsulearchive.core.domain.group.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Slice;
import site.timecapsulearchive.core.domain.group.data.dto.FinalGroupSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;


public interface GroupQueryRepository {

    Slice<FinalGroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    );

    Optional<GroupDetailDto> findGroupDetailByGroupIdAndMemberId(final Long groupId,
        final Long memberId);

    Optional<Long> getTotalGroupMemberCount(Long groupId);

    List<GroupMemberDto> findGroupMemberInfos(Long memberId, Long groupId);

}
