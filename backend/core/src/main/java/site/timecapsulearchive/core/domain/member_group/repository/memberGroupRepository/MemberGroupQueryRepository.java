package site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository;

import java.util.List;
import java.util.Optional;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupOwnerSummaryDto;

public interface MemberGroupQueryRepository {

    Optional<GroupOwnerSummaryDto> findOwnerInMemberGroup(Long groupId, Long memberId);

    Optional<Boolean> findIsOwnerByMemberIdAndGroupId(Long groupOwnerId, Long groupId);

    Optional<List<Long>> findGroupMemberIds(Long groupId);

    Optional<Long> findGroupOwnerId(Long groupId);

    List<GroupMemberDto> findGroupMemberInfos(Long memberId, Long groupId);

    Optional<Long> findGroupMembersCount(Long groupId);
    List<Long> getGroupMemberIdsByGroupId(final Long groupId);
}
