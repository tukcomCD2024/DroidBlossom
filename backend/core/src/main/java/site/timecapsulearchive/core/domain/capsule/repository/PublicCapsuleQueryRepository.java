package site.timecapsulearchive.core.domain.capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;

@Repository
@RequiredArgsConstructor
public class PublicCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<CapsuleDetailDto> findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
    ) {
        final CapsuleDetailDto detailDto = jpaQueryFactory
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
            .join(memberFriend).on(memberFriend.friend.id.eq(capsule.member.id)
                .or(memberFriend.owner.id.eq(capsule.member.id)))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC))
                .and(memberFriend.owner.id.eq(memberId)))
            .fetchFirst();

        if (detailDto.capsuleId() == null) {
            return Optional.empty();
        }

        return Optional.of(detailDto);
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    public Optional<CapsuleSummaryDto> findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        CapsuleSummaryDto.class,
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
                .join(memberFriend).on(memberFriend.friend.id.eq(capsule.member.id)
                    .or(memberFriend.owner.id.eq(capsule.member.id)))
                .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC))
                    .and(memberFriend.owner.id.eq(memberId)))
                .fetchOne()
        );
    }
}
