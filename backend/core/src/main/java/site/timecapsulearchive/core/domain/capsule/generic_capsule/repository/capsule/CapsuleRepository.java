package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;

public interface CapsuleRepository extends Repository<Capsule, Long>, CapsuleQueryRepository {

    Capsule save(Capsule capsule);

    void delete(Capsule capsule);

    Optional<Capsule> findById(Long id);

    @Query("select c from Capsule c where c.id = :capsuleId and c.member.id = :memberId")
    Optional<Capsule> findCapsuleByMemberIdAndCapsuleId(
        @Param("memberId") Long memberId,
        @Param("capsuleId") Long capsuleId
    );

    @Query("select c from Capsule c join fetch c.images where c.id = :capsuleId")
    Optional<Capsule> findCapsuleWithImageByCapsuleId(
        @Param("capsuleId") Long capsuleId
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Capsule c SET c.isOpened = true WHERE c.id = :capsuleId and c.member.id = :memberId")
    int updateIsOpenedTrue(
        @Param("memberId") Long memberId,
        @Param("capsuleId") Long capsuleId
    );

    @Query("select c "
        + "from Capsule c "
        + "join fetch c.groupCapsuleOpens "
        + "where c.id = :capsuleId "
        + "and c.type = 'GROUP' and c.group.id is not null and c.isOpened = false")
    Optional<Capsule> findNotOpenedGroupCapsuleByMemberIdAndCapsuleId(
        @Param("capsuleId") Long capsuleId
    );

    @Query("UPDATE Capsule c SET c.deletedAt = :deletedAt WHERE c.member.id = :memberId and c.group is null ")
    @Modifying
    void deleteExcludeGroupCapsuleByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    @Query("select c from Capsule c where c.group.id in :groupIds")
    List<Capsule> findCapsulesByGroupIds(@Param("groupIds") List<Long> groupIds);

    @Query("UPDATE Capsule c SET c.deletedAt = :deletedAt WHERE c.member.id = :memberId and c.id = :capsuleId")
    @Modifying
    void deleteByMemberIdAndCapsuleId(
        @Param("memberId") Long memberId,
        @Param("capsuleId") Long capsuleId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );
}
