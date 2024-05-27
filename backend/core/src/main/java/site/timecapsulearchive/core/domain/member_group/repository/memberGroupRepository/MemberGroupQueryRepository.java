package site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository;

import java.util.Optional;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupOwnerSummaryDto;

public interface MemberGroupQueryRepository {

    Optional<GroupOwnerSummaryDto> findOwnerInMemberGroup(Long groupId, Long memberId);

    Optional<Boolean> findIsOwnerByMemberIdAndGroupId(Long groupOwnerId, Long groupId);
}
