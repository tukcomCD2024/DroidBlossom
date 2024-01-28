package site.timecapsulearchive.core.domain.capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

@Repository
@RequiredArgsConstructor
public class CapsuleQueryRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory query;

    /**
     * 캡슐 타입에 따라 현재 위치에서 범위 내의 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param mbr         캡슈을 조회할 범위
     * @param capsuleType 조회할 캡슐의 타입
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    public List<CapsuleSummaryDto> findCapsuleByCurrentLocationAndCapsuleType(
        Long memberId,
        Polygon mbr,
        CapsuleType capsuleType
    ) {
        String queryString = """
            select new site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto(
                c.id,
                c.point,
                m.nickname,
                cs.imageUrl,
                c.title,
                c.dueDate,
                c.isOpened
            )
            from Capsule c
            join c.member m
            join c.capsuleSkin cs
            where ST_Contains(:mbr, c.point) and m.id=:memberId
            """;
        if (isSpecificCapsuleType(capsuleType)) {
            queryString += " and c.type = :capsuleType";
        }

        TypedQuery<CapsuleSummaryDto> query = entityManager.createQuery(queryString,
            CapsuleSummaryDto.class);
        query.setParameter("mbr", mbr);
        query.setParameter("memberId", memberId);

        if (isSpecificCapsuleType(capsuleType)) {
            query.setParameter("capsuleType", capsuleType);
        }

        return query.getResultList();
    }

    private boolean isSpecificCapsuleType(CapsuleType capsuleType) {
        return capsuleType != CapsuleType.ALL;
    }

    public Slice<SecretCapsuleSummaryDto> findSecretCapsuleSliceBySizeAndLastCapsuleId(
        int size,
        ZonedDateTime lastCapsuleCreatedAt
    ) {
        List<SecretCapsuleSummaryDto> dto = query
            .select(
                Projections.constructor(
                    SecretCapsuleSummaryDto.class,
                    capsule.id,
                    capsule.point,
                    capsule.member.nickname,
                    capsule.capsuleSkin.imageUrl,
                    capsule.title,
                    capsule.dueDate,
                    capsule.isOpened,
                    capsule.createdAt
                )
            )
            .from(capsule)
            .join(capsule.member)
            .where(capsule.createdAt.lt(lastCapsuleCreatedAt))
            .orderBy(capsule.id.desc())
            .limit(size + 1)
            .fetch();

        boolean hasNext = false;
        if (size < dto.size()) {
            dto.remove(size);
            hasNext = true;
        }

        return new SliceImpl<>(dto, Pageable.ofSize(size), hasNext);
    }
}