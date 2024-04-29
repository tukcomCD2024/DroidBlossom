package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QGroupCapsuleOpen.groupCapsuleOpen;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;

@Repository
@RequiredArgsConstructor
public class GroupCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<GroupCapsuleDetailDto> findGroupCapsuleDetailDtoByCapsuleId(
        final Long capsuleId
    ) {
        final CapsuleDetailDto capsuleDetailDto = jpaQueryFactory
            .select(
                Projections.constructor(
                    CapsuleDetailDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
                    member.nickname,
                    member.profileUrl,
                    capsule.createdAt,
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
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.GROUP)))
            .fetchFirst();

        if (capsuleDetailDto.capsuleId() == null) {
            return Optional.empty();
        }

        final List<GroupMemberSummaryDto> memberSummaryDtos = getGroupMemberSummaryDtos(
            capsuleId);

        return Optional.of(new GroupCapsuleDetailDto(capsuleDetailDto, memberSummaryDtos));
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    private List<GroupMemberSummaryDto> getGroupMemberSummaryDtos(Long capsuleId) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupMemberSummaryDto.class,
                    member.nickname,
                    member.profileUrl,
                    groupCapsuleOpen.isOpened
                )
            )
            .from(groupCapsuleOpen)
            .join(member).on(member.id.eq(groupCapsuleOpen.member.id))
            .where(groupCapsuleOpen.capsule.id.eq(capsuleId))
            .fetch();
    }

    public Optional<GroupCapsuleSummaryDto> findGroupCapsuleSummaryDtoByCapsuleId(
        final Long capsuleId) {
        final CapsuleSummaryDto capsuleSummaryDto = jpaQueryFactory
            .select(
                Projections.constructor(
                    CapsuleSummaryDto.class,
                    member.nickname,
                    member.profileUrl,
                    capsuleSkin.imageUrl,
                    capsule.title,
                    capsule.dueDate,
                    capsule.address.fullRoadAddressName,
                    capsule.address.roadName,
                    capsule.isOpened,
                    capsule.createdAt
                )
            )
            .from(capsule)
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.GROUP)))
            .groupBy(capsule.id)
            .fetchOne();

        if (capsuleSummaryDto == null) {
            return Optional.empty();
        }

        final List<GroupMemberSummaryDto> memberSummaryDtos = getGroupMemberSummaryDtos(
            capsuleId);

        return Optional.of(new GroupCapsuleSummaryDto(capsuleSummaryDto, memberSummaryDtos));
    }
}
