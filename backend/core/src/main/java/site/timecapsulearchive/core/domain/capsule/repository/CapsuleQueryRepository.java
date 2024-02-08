package site.timecapsulearchive.core.domain.capsule.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.dto.mapper.CapsuleMapper;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.MySecreteCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.SecretCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

@Repository
@RequiredArgsConstructor
public class CapsuleQueryRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final CapsuleMapper capsuleMapper;

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
        TypedQuery<CapsuleSummaryDto> query = generateSelectQueryOnCapsuleSummaryDtoWith(
            capsuleType);

        assignParameter(memberId, mbr, capsuleType, query);

        return query.getResultList();
    }

    private TypedQuery<CapsuleSummaryDto> generateSelectQueryOnCapsuleSummaryDtoWith(
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

        return entityManager.createQuery(queryString, CapsuleSummaryDto.class);
    }

    private void assignParameter(
        Long memberId,
        Polygon mbr,
        CapsuleType capsuleType,
        TypedQuery<CapsuleSummaryDto> query
    ) {
        query.setParameter("mbr", mbr);
        query.setParameter("memberId", memberId);

        if (capsuleType != CapsuleType.ALL) {
            query.setParameter("capsuleType", capsuleType);
        }
    }

    public Optional<SecretCapsuleSummaryDto> findSecretCapsuleSummaryByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        SecretCapsuleSummaryDto.class,
                        capsule.member.nickname,
                        capsule.member.profileUrl,
                        capsule.capsuleSkin.imageUrl,
                        capsule.title,
                        capsule.dueDate,
                        capsule.address.fullRoadAddressName,
                        capsule.address.roadName,
                        capsule.isOpened,
                        capsule.createdAt
                    )
                )
                .from(capsule)
                .where(capsule.id.eq(capsuleId).and(capsule.member.id.eq(memberId))
                    .and(capsule.type.eq(CapsuleType.SECRET)))
                .fetchOne()
        );
    }

    public Optional<SecretCapsuleDetailDto> findSecretCapsuleDetailByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        SecretCapsuleDetailDto detailDto = jpaQueryFactory
            .select(
                Projections.constructor(
                    SecretCapsuleDetailDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
                    member.nickname,
                    member.profileUrl,
                    capsule.createdAt,
                    capsule.address.fullRoadAddressName,
                    capsule.title,
                    capsule.content,
                    groupConcatDistinct(image.imageUrl),
                    groupConcatDistinct(video.videoUrl),
                    capsule.isOpened,
                    capsule.type
                )
            )
            .from(capsule)
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(
                capsule.id.eq(capsuleId)
                    .and(capsule.member.id.eq(memberId))
                    .and(capsule.type.eq(CapsuleType.SECRET))
            )
            .fetchFirst();

        return Optional.ofNullable(detailDto);
    }

    private StringExpression groupConcatDistinct(StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    public Slice<MySecreteCapsuleDto> findSecretCapsuleSliceByMemberIdAndCreatedAt(
        Long memberId,
        int size,
        ZonedDateTime createdAt
    ) {
        List<MySecreteCapsuleDto> mySecretCapsules = findMySecretCapsulesByMemberIdAndCreatedAt(
            memberId, size, createdAt
        );

        boolean hasNext = canMoreRead(size, mySecretCapsules.size());
        if (hasNext) {
            mySecretCapsules.remove(size);
        }

        return new SliceImpl<>(mySecretCapsules, Pageable.ofSize(size), hasNext);
    }

    private List<MySecreteCapsuleDto> findMySecretCapsulesByMemberIdAndCreatedAt(
        Long memberId,
        int size,
        ZonedDateTime createdAt
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    MySecreteCapsuleDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
                    capsule.createdAt,
                    capsule.title,
                    capsule.isOpened,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .where(
                capsule.member.id.eq(memberId),
                capsule.createdAt.lt(createdAt),
                capsule.type.eq(CapsuleType.SECRET)
            )
            .orderBy(capsule.id.desc())
            .limit(size + 1)
            .fetch();
    }

    private boolean canMoreRead(int size, int capsuleSize) {
        return capsuleSize > size;
    }
}