package site.timecapsulearchive.core.domain.capsule.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;

public interface CapsuleRepository extends Repository<Capsule, Long> {

    Capsule save(Capsule capsule);

    @Query("select c from Capsule c where c.id = :capsuleId and c.member.id = :memberId")
    Optional<Capsule> findCapsuleByMemberIdAndCapsuleId(
        @Param("memberId") Long memberId,
        @Param("capsuleId") Long capsuleId
    );

    @Modifying
    @Transactional
    @Query("UPDATE Capsule c SET c.isOpened = true WHERE c.id = :capsuleId and c.member.id = :memberId")
    void updateIsOpenedTrue(
        @Param("memberId") Long memberId,
        @Param("capsuleId") Long capsuleId
    );
}
