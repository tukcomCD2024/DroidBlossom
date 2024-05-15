package site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.member_group.entity.GroupInvite;

public interface GroupInviteRepository extends Repository<GroupInvite, Long>,
    GroupInviteQueryRepository {

    void save(GroupInvite groupInvite);

    int deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(Long groupId, Long groupOwnerId,
        Long groupMemberId);

    @Query("delete from GroupInvite gi where gi.id in :groupInviteIds")
    @Modifying
    void bulkDelete(@Param("groupInviteIds") List<Long> groupInviteIds);
}

