package site.timecapsulearchive.core.domain.group.repository.memberGroupRepository;

import java.util.Optional;
import site.timecapsulearchive.core.domain.group.data.dto.GroupOwnerSummaryDto;

public interface MemberGroupQueryRepository {

    Optional<GroupOwnerSummaryDto> findOwnerInMemberGroup(Long groupId, Long memberId);

    Optional<Boolean> findIsOwnerByMemberIdAndGroupId(Long memberId, Long groupId);
}
