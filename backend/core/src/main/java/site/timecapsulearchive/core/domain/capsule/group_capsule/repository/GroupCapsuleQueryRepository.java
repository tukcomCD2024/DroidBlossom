package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QGroupCapsuleOpen.groupCapsuleOpen;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
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
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSliceRequestDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.member.entity.QMember;
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class GroupCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<GroupCapsuleDetailDto> findGroupCapsuleDetailDtoByCapsuleId(
        final Long capsuleId
    ) {
        final QMember owner = new QMember("owner");
        final QMember groupMember = new QMember("groupMember");

        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(capsule)
            .join(owner).on(capsule.member.id.eq(owner.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .join(groupCapsuleOpen).on(groupCapsuleOpen.capsule.id.eq(capsuleId))
            .join(groupMember).on(groupMember.id.eq(groupCapsuleOpen.member.id))
            .groupBy(groupCapsuleOpen.id)
            .where(groupCapsuleOpen.capsule.id.eq(capsuleId))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.GROUP)))
            .transform(
                groupBy(capsule.id).as(
                    Projections.constructor(
                        GroupCapsuleDetailDto.class,
                        Projections.constructor(
                            CapsuleDetailDto.class,
                            capsule.id,
                            capsuleSkin.imageUrl,
                            capsule.dueDate,
                            owner.nickname,
                            owner.profileUrl,
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
                        ),
                        list(Projections.constructor(GroupMemberSummaryDto.class,
                            groupMember.nickname,
                            groupMember.profileUrl,
                            groupCapsuleOpen.isOpened)
                        )
                    )
                )
            ).get(capsuleId));
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    public Optional<GroupCapsuleSummaryDto> findGroupCapsuleSummaryDtoByCapsuleId(
        final Long capsuleId) {
        final QMember owner = new QMember("owner");
        final QMember groupMember = new QMember("groupMember");

        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(capsule)
            .join(owner).on(capsule.member.id.eq(owner.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id)).join(groupCapsuleOpen)
            .on(groupCapsuleOpen.capsule.id.eq(capsuleId))
            .join(groupMember).on(groupMember.id.eq(groupCapsuleOpen.member.id))
            .groupBy(groupCapsuleOpen.id)
            .where(groupCapsuleOpen.capsule.id.eq(capsuleId))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.GROUP)))
            .transform(
                groupBy(capsule.id).as(
                    Projections.constructor(
                        GroupCapsuleSummaryDto.class,
                        Projections.constructor(
                            CapsuleSummaryDto.class,
                            owner.nickname,
                            owner.profileUrl,
                            capsuleSkin.imageUrl,
                            capsule.title,
                            capsule.dueDate,
                            capsule.point,
                            capsule.address.fullRoadAddressName,
                            capsule.address.roadName,
                            capsule.isOpened,
                            capsule.createdAt
                        ),
                        list(Projections.constructor(GroupMemberSummaryDto.class,
                            groupMember.nickname,
                            groupMember.profileUrl,
                            groupCapsuleOpen.isOpened)
                        )
                    )

                )
            ).get(capsuleId));
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
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
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

    public Slice<CapsuleBasicInfoDto> findGroupCapsuleSlice(final GroupCapsuleSliceRequestDto dto) {
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
}
