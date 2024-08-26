package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupSpecificCapsuleSliceRequestDto;
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class GroupCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<GroupCapsuleDetailDto> findGroupCapsuleDetailDtoByCapsuleId(
        final Long capsuleId
    ) {
        return Optional.ofNullable(jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupCapsuleDetailDto.class,
                    group.id,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
                    member.id,
                    member.nickname,
                    member.profileUrl,
                    capsule.createdAt,
                    capsule.point,
                    capsule.address.fullRoadAddressName,
                    capsule.address.roadName,
                    capsule.title,
                    capsule.content,
                    groupConcatDistinct(image.imageUrl),
                    groupConcatDistinct(video.videoUrl),
                    capsule.isOpened,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsule.member, member)
            .join(capsuleSkin)
            .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
            .join(group).on(capsule.group.eq(group), group.deletedAt.isNull())
            .leftJoin(image).on(capsule.eq(image.capsule), image.deletedAt.isNull())
            .leftJoin(video).on(capsule.eq(video.capsule), video.deletedAt.isNull())
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.GROUP)))
            .fetchOne()
        );
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT({0})", expression);
    }

    public Optional<GroupCapsuleSummaryDto> findGroupCapsuleSummaryDtoByCapsuleId(
        final Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        GroupCapsuleSummaryDto.class,
                        group.id,
                        group.groupName,
                        group.groupProfileUrl,
                        member.id,
                        member.nickname,
                        member.profileUrl,
                        capsuleSkin.imageUrl,
                        capsule.title,
                        capsule.dueDate,
                        capsule.point,
                        capsule.address.fullRoadAddressName,
                        capsule.address.roadName,
                        capsule.isOpened,
                        capsule.createdAt
                    )
                )
                .from(capsule)
                .join(capsuleSkin)
                .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
                .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
                .join(group).on(capsule.group.eq(group), group.deletedAt.isNull())
                .where(capsule.id.eq(capsuleId)
                    .and(capsule.type.eq(CapsuleType.GROUP))
                )
                .fetchOne()
        );
    }

    public Slice<CapsuleBasicInfoDto> findMyGroupCapsuleSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<CapsuleBasicInfoDto> groupCapsules = jpaQueryFactory
            .select(
                Projections.constructor(
                    CapsuleBasicInfoDto.class,
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
            .join(capsuleSkin)
            .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
            .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
            .where(capsule.member.id.eq(memberId)
                .and(capsule.createdAt.lt(createdAt))
                .and(capsule.type.eq(CapsuleType.GROUP)))
            .orderBy(capsule.createdAt.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, groupCapsules);
    }

    public boolean findGroupCapsuleExistByGroupId(Long groupId) {
        Integer count = jpaQueryFactory
            .selectOne()
            .from(capsule)
            .where(capsule.group.id.eq(groupId))
            .fetchFirst();

        return count != null;
    }

    public Long findGroupCapsuleCount(final Long groupId) {
        return jpaQueryFactory.select(capsule.count())
            .from(capsule)
            .where(capsule.group.id.eq(groupId))
            .fetchOne();
    }

    public Slice<CapsuleBasicInfoDto> findGroupSpecificCapsuleSlice(
        final GroupSpecificCapsuleSliceRequestDto dto) {
        final List<CapsuleBasicInfoDto> groupCapsuleDtos = jpaQueryFactory
            .select(
                Projections.constructor(
                    CapsuleBasicInfoDto.class,
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
            .where(capsule.group.id.eq(dto.groupId())
                .and(capsuleIdPagingCursorCondition(dto.lastCapsuleId())))
            .orderBy(capsule.id.desc())
            .limit(dto.size() + 1)
            .fetch();

        return SliceUtil.makeSlice(dto.size(), groupCapsuleDtos);
    }

    private BooleanExpression capsuleIdPagingCursorCondition(final Long capsuleId) {
        if (capsuleId == null) {
            return null;
        }

        return capsule.id.lt(capsuleId);
    }

    public Slice<GroupCapsuleDto> findGroupCapsuleSlice(
        final int size,
        final Long lastCapsuleId,
        final List<Long> groupIds
    ) {
        final List<GroupCapsuleDto> groupCapsules = jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupCapsuleDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
                    group.id,
                    group.groupName,
                    group.groupProfileUrl,
                    member.nickname,
                    member.profileUrl,
                    capsule.createdAt,
                    capsule.point,
                    capsule.address.fullRoadAddressName,
                    capsule.address.roadName,
                    capsule.title,
                    capsule.content,
                    groupConcatDistinct(image.imageUrl),
                    groupConcatDistinct(video.videoUrl),
                    capsule.isOpened,
                    capsule.type
                )
            )
            .from(capsule)
            .join(capsuleSkin)
            .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
            .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
            .join(group).on(capsule.group.eq(group), group.deletedAt.isNull())
            .leftJoin(image).on(capsule.eq(image.capsule), image.deletedAt.isNull())
            .leftJoin(video).on(capsule.eq(video.capsule), video.deletedAt.isNull())
            .where(capsule.type.eq(CapsuleType.GROUP)
                .and(capsule.group.id.in(groupIds))
                .and(capsuleIdPagingCursorCondition(lastCapsuleId)))
            .groupBy(capsule.id)
            .orderBy(capsule.id.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, groupCapsules);
    }
}
