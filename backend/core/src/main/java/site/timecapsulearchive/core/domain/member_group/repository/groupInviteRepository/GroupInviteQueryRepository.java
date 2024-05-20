package site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository;

import java.util.List;

public interface GroupInviteQueryRepository {

    void bulkSave(final Long groupOwnerId, final Long groupId, final List<Long> groupMemberIds);

    List<Long> findGroupInviteIdsByGroupIdAndGroupOwnerId(final Long groupId, final Long memberId);
}
