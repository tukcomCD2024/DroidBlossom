package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;

@Repository
@RequiredArgsConstructor
public class CapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * AR에서 캡슐을 찾기 위해 캡슐 타입에 따라 현재 위치에서 범위 내의 사용자가 만든 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param mbr         캡슐을 조회할 범위(최소사각형)
     * @param capsuleType 조회할 캡슐의 타입
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     * @see site.timecapsulearchive.core.global.geography.GeoTransformManager
     */
    public List<NearbyARCapsuleSummaryDto> findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    NearbyARCapsuleSummaryDto.class,
                    capsule.id,
                    capsule.point,
                    member.nickname,
                    capsuleSkin.imageUrl,
                    capsule.title,
                    capsule.dueDate,
                    capsule.isOpened,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsule.capsuleSkin, capsuleSkin)
            .join(capsule.member, member)
            .where(ST_Contains(mbr, capsule.point).and(capsuleFilter(capsuleType, memberId)))
            .fetch();
    }

    private BooleanExpression capsuleFilter(CapsuleType capsuleType, Long memberId) {
        return switch (capsuleType) {
            case ALL -> capsule.member.id.eq(memberId);
            case TREASURE -> capsule.type.eq(capsuleType);
            default -> capsule.type.eq(capsuleType).and(capsule.member.id.eq(memberId));
        };
    }

    /**
     * 지도에서 캡슐을 찾기 위해 캡슐 타입에 따라 현재 위치에서 범위 내의 사용자가 만든 캡슐을 조회한다.
     *
     * @param memberId    범위 내의 캡슙을 조회할 멤버 id
     * @param mbr         캡슐을 조회할 범위(최소사각형), <code>GeoTransformManager</code> 참조
     * @param capsuleType 조회할 캡슐의 타입
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    public List<NearbyCapsuleSummaryDto> findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    NearbyCapsuleSummaryDto.class,
                    capsule.id,
                    capsule.point,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsule.capsuleSkin, capsuleSkin)
            .join(capsule.member, member)
            .where(ST_Contains(mbr, capsule.point).and(capsuleFilter(capsuleType, memberId)))
            .fetch();
    }

    /**
     * 지도에서 사용자의 친구들의 캡슐을 찾기 위해 현재 위치에서 범위 내의 사용자의 친구가 만든 캡슐을 조회한다.
     *
     * @param friendIds 범위 내의 조회할 사용자 목록
     * @param mbr       캡슐을 조회할 범위(최소사각형), <code>GeoTransformManager</code> 참조
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    public List<NearbyCapsuleSummaryDto> findFriendsCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        List<Long> friendIds,
        Polygon mbr
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    NearbyCapsuleSummaryDto.class,
                    capsule.id,
                    capsule.point,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsule.capsuleSkin, capsuleSkin)
            .join(capsule.member, member)
            .where(ST_Contains(mbr, capsule.point).and(capsule.member.id.in(friendIds))
                .and(capsule.type.eq(CapsuleType.PUBLIC)))
            .fetch();
    }

    private BooleanExpression ST_Contains(Polygon mbr, ComparablePath<Point> point) {
        return Expressions.booleanTemplate("ST_Contains({0}, {1})", mbr, point);
    }

    /**
     * AR에서 사용자의 친구들의 캡슐을 찾기 위해 현재 위치에서 범위 내의 사용자의 친구가 만든 캡슐을 조회한다.
     *
     * @param friendIds 범위 내의 조회할 사용자 목록
     * @param mbr       캡슐을 조회할 범위(최소사각형), <code>GeoTransformManager</code> 참조
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    public List<NearbyARCapsuleSummaryDto> findFriendsARCapsulesByCurrentLocation(
        List<Long> friendIds,
        Polygon mbr
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    NearbyARCapsuleSummaryDto.class,
                    capsule.id,
                    capsule.point,
                    member.nickname,
                    capsuleSkin.imageUrl,
                    capsule.title,
                    capsule.dueDate,
                    capsule.isOpened,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsule.capsuleSkin, capsuleSkin)
            .join(capsule.member, member)
            .where(ST_Contains(mbr, capsule.point).and(capsule.member.id.in(friendIds))
                .and(capsule.type.eq(CapsuleType.PUBLIC)))
            .fetch();
    }
}