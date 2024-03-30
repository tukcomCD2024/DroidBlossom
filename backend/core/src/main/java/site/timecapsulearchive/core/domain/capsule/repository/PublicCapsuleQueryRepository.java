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
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleSummaryDto;

@Repository
@RequiredArgsConstructor
public class PublicCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<PublicCapsuleDetailDto> findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
    ) {
        final PublicCapsuleDetailDto detailDto = jpaQueryFactory
            .select(
                Projections.constructor(
                    PublicCapsuleDetailDto.class,
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
                    capsule.type,
                    memberFriend.id.isNotNull()
                )
            )
            .from(capsule)
            .join(member).on(capsule.member.id.eq(member.id))
            .join(member)
            .on(memberFriend.friend.id.eq(member.id).and(memberFriend.owner.id.eq(memberId)))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC)))
            .fetchFirst();

        return Optional.ofNullable(detailDto);
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    public Optional<PublicCapsuleSummaryDto> findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
        Long memberId,
        Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        PublicCapsuleSummaryDto.class,
                        capsule.member.nickname,
                        capsule.member.profileUrl,
                        capsule.capsuleSkin.imageUrl,
                        capsule.title,
                        capsule.dueDate,
                        capsule.address.fullRoadAddressName,
                        capsule.address.roadName,
                        capsule.isOpened,
                        capsule.createdAt,
                        memberFriend.id.isNotNull()
                    )
                )
                .from(capsule)
                .on(memberFriend.friend.id.eq(member.id).and(memberFriend.owner.id.eq(memberId)))
                .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC)))
                .fetchOne()
        );
    }
}
