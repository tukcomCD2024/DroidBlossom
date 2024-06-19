package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.capsule;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.treasure_capsule.data.dto.TreasureCapsuleSummaryDto;

@Repository
@RequiredArgsConstructor
public class CapsuleQueryRepositoryImpl implements CapsuleQueryRepository {

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
    @Override
    public List<NearbyARCapsuleSummaryDto> findARCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    ) {
        JPAQuery<NearbyARCapsuleSummaryDto> dynamicQuery = jpaQueryFactory
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
            .join(capsule.member, member);

        joinMemberGroupForGroupCapsule(memberId, capsuleType, dynamicQuery);

        return dynamicQuery
            .where(ST_Contains(mbr, capsule.point).and(capsuleFilter(capsuleType, memberId)))
            .fetch();
    }

    private BooleanExpression capsuleFilter(CapsuleType capsuleType, Long memberId) {
        return switch (capsuleType) {
            case ALL -> capsule.member.id.eq(memberId);
            case TREASURE -> capsule.type.eq(capsuleType);
            case GROUP -> capsule.type.eq(CapsuleType.GROUP);
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
    @Override
    public List<NearbyCapsuleSummaryDto> findCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final Long memberId,
        final Polygon mbr,
        final CapsuleType capsuleType
    ) {
        JPAQuery<NearbyCapsuleSummaryDto> dynamicQuery = jpaQueryFactory
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
            .join(capsule.member, member);

        joinMemberGroupForGroupCapsule(memberId, capsuleType, dynamicQuery);

        return dynamicQuery
            .where(ST_Contains(mbr, capsule.point).and(capsuleFilter(capsuleType, memberId)))
            .fetch();
    }

    private void joinMemberGroupForGroupCapsule(Long memberId, CapsuleType capsuleType,
        JPAQuery<?> dynamicQuery) {
        if (capsuleType.equals(CapsuleType.GROUP)) {
            dynamicQuery
                .join(memberGroup).on(memberGroup.member.id.eq(capsule.member.id)
                    .and(memberGroup.member.id.eq(memberId))
                );
        }
    }

    /**
     * 지도에서 사용자의 친구들의 캡슐을 찾기 위해 현재 위치에서 범위 내의 사용자의 친구가 만든 캡슐을 조회한다.
     *
     * @param friendIds 범위 내의 조회할 사용자 목록
     * @param mbr       캡슐을 조회할 범위(최소사각형), <code>GeoTransformManager</code> 참조
     * @return 범위 내에 조회된 캡슐들의 요약 정보들을 반환한다.
     */
    @Override
    public List<NearbyCapsuleSummaryDto> findFriendsCapsuleSummaryDtosByCurrentLocationAndCapsuleType(
        final List<Long> friendIds,
        final Polygon mbr
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
    @Override
    public List<NearbyARCapsuleSummaryDto> findFriendsARCapsulesByCurrentLocation(
        final List<Long> friendIds,
        final Polygon mbr
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

    @Override
    public Optional<TreasureCapsuleSummaryDto> findTreasureCapsuleSummary(final Long capsuleId) {
        return Optional.ofNullable(jpaQueryFactory
            .select(
                Projections.constructor(
                    TreasureCapsuleSummaryDto.class,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
                    capsule.address.fullRoadAddressName,
                    capsule.address.roadName
                )
            )
            .from(capsule)
            .join(capsule.capsuleSkin, capsuleSkin)
            .where(capsule.id.eq(capsuleId))
            .fetchOne()
        );
    }
}