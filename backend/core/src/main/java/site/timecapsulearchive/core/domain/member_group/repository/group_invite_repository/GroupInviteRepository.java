package site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository;

import java.time.ZonedDateTime;
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

    @Query("delete from GroupInvite gi where gi.id in :groupInviteIds")
    @Modifying
    void bulkDelete(@Param("groupInviteIds") List<Long> groupInviteIds);

    Optional<GroupInvite> findGroupInviteByIdAndGroupOwnerId(Long groupInviteId, Long groupOwnerId);

    @Query("UPDATE GroupInvite gi SET gi.deletedAt = :deletedAt WHERE gi.groupMember.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("UPDATE GroupInvite gi SET gi.deletedAt = :deletedAt WHERE gi.group.id in :groupIds")
    @Modifying
    void deleteByGroupIds(
        @Param("groupIds") List<Long> groupIds,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    Optional<GroupInvite> findGroupInviteByGroupIdAndGroupMemberId(Long groupId, Long groupMemberId);
}

