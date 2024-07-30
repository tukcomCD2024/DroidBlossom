package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.capsule.entity.GroupCapsuleOpen;

public interface GroupCapsuleOpenRepository extends Repository<GroupCapsuleOpen, Long>,
    GroupCapsuleOpenQueryRepository {

    void save(GroupCapsuleOpen groupCapsuleOpen);

    @Query("UPDATE GroupCapsuleOpen gco SET gco.deletedAt = :deletedAt WHERE gco.member.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("UPDATE GroupCapsuleOpen gco SET gco.deletedAt = :deletedAt WHERE gco.capsule.id in :groupCapsuleIds")
    @Modifying
    void deleteByCapsuleIds(
        @Param("groupCapsuleIds") List<Long> groupCapsuleIds,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("UPDATE GroupCapsuleOpen gco SET gco.deletedAt = :deletedAt WHERE gco.member.id = :memberId and gco.capsule.id = :capsuleId")
    @Modifying
    void deleteByMemberIdAndCapsuleId(
        @Param("memberId") Long memberId,
        @Param("capsuleId") Long capsuleId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
