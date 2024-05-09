package site.timecapsulearchive.core.domain.group.repository.groupInviteRepository;

import org.springframework.data.repository.Repository;
import site.timecapsulearchive.core.domain.group.entity.GroupInvite;

public interface GroupInviteRepository extends Repository<GroupInvite, Long>,
    GroupInviteQueryRepository {

    void save(GroupInvite groupInvite);

    int deleteGroupInviteByGroupOwnerIdAndGroupMemberId(Long groupOwnerId, Long groupMemberId);
}
