package site.timecapsulearchive.core.domain.capsule.treasure_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TreasureCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Long> deleteTreasureCapsule(final Long capsuleId) {
        return Optional.of(jpaQueryFactory
            .delete(capsule)
            .where(capsule.id.eq(capsuleId))
            .execute()
        );
    }

}
