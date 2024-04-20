package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;

@Repository
@RequiredArgsConstructor
public class CapsuleQueryRepository {

    private final EntityManager entityManager;

    /**
     * 캡슐 타입에 따라 현재 위치에서 범위 내의 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param mbr         캡슈을 조회할 범위
     * @param capsuleType 조회할 캡슐의 타입
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    public List<NearbyARCapsuleSummaryDto> findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    ) {
        final TypedQuery<NearbyARCapsuleSummaryDto> query = generateSelectQueryOnARCapsuleSummaryDtoWith(
            capsuleType);

        assignARCapsuleParameter(memberId, mbr, capsuleType, query);

        return query.getResultList();
    }

    private TypedQuery<NearbyARCapsuleSummaryDto> generateSelectQueryOnARCapsuleSummaryDtoWith(
        final CapsuleType capsuleType
    ) {
        String queryString = """
            select new site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto(
                c.id,
                c.point,
                m.nickname,
                cs.imageUrl,
                c.title,
                c.dueDate,
                c.isOpened,
                c.type
            )
            from Capsule c
            join c.member m
            join c.capsuleSkin cs
            where ST_Contains(:mbr, c.point) and m.id=:memberId
            """;

        if (capsuleType != CapsuleType.ALL) {
            queryString += " and c.type = :capsuleType";
        }

        return entityManager.createQuery(queryString, NearbyARCapsuleSummaryDto.class);
    }

    private void assignARCapsuleParameter(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType,
        final TypedQuery<NearbyARCapsuleSummaryDto> query
    ) {
        query.setParameter("mbr", mbr);
        query.setParameter("memberId", memberId);

        if (capsuleType != CapsuleType.ALL) {
            query.setParameter("capsuleType", capsuleType);
        }
    }

    public List<NearbyCapsuleSummaryDto> findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    ) {
        final TypedQuery<NearbyCapsuleSummaryDto> query = generateSelectQueryOnCapsuleSummaryDtoWith(
            capsuleType);

        assignCapsuleParameter(memberId, mbr, capsuleType, query);

        return query.getResultList();
    }

    private TypedQuery<NearbyCapsuleSummaryDto> generateSelectQueryOnCapsuleSummaryDtoWith(
        final CapsuleType capsuleType
    ) {
        String queryString = """
            select new site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto(
                c.id,
                c.point,
                c.type
            )
            from Capsule c
            join c.member m
            where ST_Contains(:mbr, c.point) and m.id=:memberId
            """;

        if (capsuleType != CapsuleType.ALL) {
            queryString += " and c.type = :capsuleType";
        }

        return entityManager.createQuery(queryString, NearbyCapsuleSummaryDto.class);
    }

    private void assignCapsuleParameter(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType,
        final TypedQuery<NearbyCapsuleSummaryDto> query
    ) {
        query.setParameter("mbr", mbr);
        query.setParameter("memberId", memberId);

        if (capsuleType != CapsuleType.ALL) {
            query.setParameter("capsuleType", capsuleType);
        }
    }
}