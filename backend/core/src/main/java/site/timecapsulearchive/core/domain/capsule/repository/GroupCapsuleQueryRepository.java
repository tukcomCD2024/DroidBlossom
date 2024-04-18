package site.timecapsulearchive.core.domain.capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.group.entity.QMemberGroup.memberGroup;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;

@Repository
@RequiredArgsConstructor
public class GroupCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<GroupCapsuleDetailDto> findGroupCapsuleDetailDtoByIds(
        final Long memberId,
        final Long groupId,
        final Long capsuleId
    ) {
        final GroupCapsuleDetailDto detailDto = jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupCapsuleDetailDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    Projections.list(
                        Projections.constructor(
                            GroupMemberSummaryDto.class,
                            member.nickname,
                            member.profileUrl,
                            capsule.isOpened
                        )
                    ),
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
            .join(memberGroup)
            .on(memberGroup.member.id.eq(memberId).and(memberGroup.group.id.eq(groupId)))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.GROUP)))
            .fetchFirst();

        if (detailDto.capsuleId() == null) {
            return Optional.empty();
        }

        return Optional.of(detailDto);
    }


    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

}
