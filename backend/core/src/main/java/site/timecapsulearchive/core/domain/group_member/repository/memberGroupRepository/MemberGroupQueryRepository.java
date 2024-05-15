package site.timecapsulearchive.core.domain.group_member.repository.memberGroupRepository;

import java.util.Optional;
import site.timecapsulearchive.core.domain.group_member.data.GroupOwnerSummaryDto;

public interface MemberGroupQueryRepository {

    Optional<GroupOwnerSummaryDto> findOwnerInMemberGroup(Long groupId, Long memberId);

    Optional<Boolean> findIsOwnerByMemberIdAndGroupId(Long groupOwnerId, Long groupId);
}
