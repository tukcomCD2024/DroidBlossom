package site.timecapsulearchive.core.domain.group.repository.groupInviteRepository;

import java.util.List;

public interface GroupInviteQueryRepository {

    void bulkSave(final Long groupOwnerId, final List<Long> groupMemberIds);
}