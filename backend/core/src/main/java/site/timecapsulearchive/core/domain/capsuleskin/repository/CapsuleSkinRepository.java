package site.timecapsulearchive.core.domain.capsuleskin.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import site.timecapsulearchive.core.domain.capsuleskin.entity.CapsuleSkin;

public interface CapsuleSkinRepository extends Repository<CapsuleSkin, Long>,
    CapsuleSkinQueryRepository {

    Optional<CapsuleSkin> save(CapsuleSkin capsuleSkin);

    Optional<CapsuleSkin> findCapsuleSkinById(Long capsuleSkinId);

    @Query("UPDATE CapsuleSkin cs SET cs.deletedAt = :deletedAt WHERE cs.member.id = :memberId")
    @Modifying
    void deleteByMemberId(
        @Param("memberId") Long memberId,
        @Param("deletedAt") ZonedDateTime deletedAt
    );

    void delete(CapsuleSkin capsuleSkin);

    Optional<CapsuleSkin> findCapsuleSkinByMemberIdAndId(Long memberId, Long capsuleSkinId);
}
