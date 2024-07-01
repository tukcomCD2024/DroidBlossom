package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import java.util.List;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;

public interface GroupCapsuleOpenQueryRepository {
  
    void bulkSave(final Long groupId, final List<Long> groupMemberIds, final Capsule capsule);

    List<GroupCapsuleMemberDto> findGroupCapsuleMembers(final Long capsuleId, final Long groupId);
}
