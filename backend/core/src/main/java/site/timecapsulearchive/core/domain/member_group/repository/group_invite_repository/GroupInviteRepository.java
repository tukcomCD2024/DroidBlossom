package site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.member_group.entity.GroupInvite;

public interface GroupInviteRepository extends Repository<GroupInvite, Long>,
    GroupInviteQueryRepository {

    void save(GroupInvite groupInvite);

    void delete(GroupInvite groupInvite);

    @Query("delete from GroupInvite gi "
        + "where gi.group.id =:groupId "
        + "and gi.groupOwner.id =:groupOwnerId "
        + "and gi.groupMember.id =:groupMemberId"
    )
    @Modifying
    int deleteGroupInviteByGroupIdAndGroupOwnerIdAndGroupMemberId(
        @Param("groupId") Long groupId,
        @Param("groupOwnerId") Long groupOwnerId,
        @Param("groupMemberId") Long groupMemberId
    );

    @Query("delete from GroupInvite gi where gi.id in :groupInviteIds")
    @Modifying
    void bulkDelete(@Param("groupInviteIds") List<Long> groupInviteIds);

    Optional<GroupInvite> findGroupInviteByIdAndGroupOwnerId(Long groupInviteId, Long groupOwnerId);
}

