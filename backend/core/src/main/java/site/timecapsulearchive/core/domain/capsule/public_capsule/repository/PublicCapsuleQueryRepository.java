package site.timecapsulearchive.core.domain.capsule.public_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleSummaryDto;
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class PublicCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static BooleanExpression eqMemberIdOrFriendId(Long memberId) {
        return eqMemberId(memberId).or(eqFriendId(memberId));
    }

    private static BooleanExpression eqMemberId(Long memberId) {
        return capsule.member.id.eq(memberId);
    }

    private static BooleanExpression eqFriendId(Long memberId) {
        return memberFriend.friend.id.eq(memberId);
    }

    public Optional<PublicCapsuleDetailDto> findPublicCapsuleDetailDtosByMemberIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
    ) {
        PublicCapsuleDetailDto publicCapsuleDetailDto = jpaQueryFactory
            .select(
                Projections.constructor(
                    PublicCapsuleDetailDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
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
                    capsule.type,
                    new CaseBuilder()
                        .when(eqMemberId(memberId)).then(true)
                        .otherwise(false)
                )
            )
            .from(capsule)
            .join(capsuleSkin)
            .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
            .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
            .leftJoin(memberFriend).on(capsule.member.eq(memberFriend.owner)
                .or(capsule.member.eq(memberFriend.friend)), memberFriend.deletedAt.isNull())
            .leftJoin(image).on(capsule.eq(image.capsule), image.deletedAt.isNull())
            .leftJoin(video).on(capsule.eq(video.capsule), video.deletedAt.isNull())
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC))
                .and(eqMemberIdOrFriendId(memberId)))
            .fetchFirst();

        if (publicCapsuleDetailDto.capsuleId() == null) {
            return Optional.empty();
        }

        return Optional.of(publicCapsuleDetailDto);
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    public Optional<PublicCapsuleSummaryDto> findPublicCapsuleSummaryDtosByMemberIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        PublicCapsuleSummaryDto.class,
                        member.nickname,
                        member.profileUrl,
                        capsuleSkin.imageUrl,
                        capsule.title,
                        capsule.dueDate,
                        capsule.point,
                        capsule.address.fullRoadAddressName,
                        capsule.address.roadName,
                        capsule.isOpened,
                        capsule.createdAt,
                        new CaseBuilder()
                            .when(eqMemberId(memberId)).then(true)
                            .otherwise(false)
                    )
                )
                .from(capsule)
                .join(capsuleSkin)
                .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
                .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
                .leftJoin(memberFriend).on(capsule.member.eq(memberFriend.owner)
                    .or(capsule.member.eq(memberFriend.friend)), memberFriend.deletedAt.isNull())
                .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC))
                    .and(eqMemberIdOrFriendId(memberId)))
                .groupBy(capsule.id)
                .fetchOne()
        );
    }

    public Slice<PublicCapsuleDetailDto> findPublicCapsulesDtoMadeByFriend(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {

        final List<Long> memberIds = jpaQueryFactory
            .select(memberFriend.friend.id)
            .from(memberFriend)
            .where(memberFriend.owner.id.eq(memberId))
            .fetch();
        memberIds.add(memberId);

        final List<PublicCapsuleDetailDto> publicCapsuleDetailDtos = jpaQueryFactory
            .select(
                Projections.constructor(
                    PublicCapsuleDetailDto.class,
                    capsule.id,
                    capsuleSkin.imageUrl,
                    capsule.dueDate,
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
                    capsule.type,
                    new CaseBuilder()
                        .when(eqMemberId(memberId)).then(true)
                        .otherwise(false)
                )
            )
            .from(capsule)
            .join(capsuleSkin)
            .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
            .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
            .leftJoin(image).on(capsule.eq(image.capsule), image.deletedAt.isNull())
            .leftJoin(video).on(capsule.eq(video.capsule), video.deletedAt.isNull())
            .where(capsule.member.id.in(memberIds)
                .and(capsule.createdAt.lt(createdAt))
                .and(capsule.type.eq(CapsuleType.PUBLIC)))
            .groupBy(capsule.id)
            .orderBy(capsule.createdAt.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, publicCapsuleDetailDtos);
    }


    public Slice<CapsuleBasicInfoDto> findMyPublicCapsuleSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<CapsuleBasicInfoDto> publicCapsules = jpaQueryFactory
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
                .and(capsule.type.eq(CapsuleType.PUBLIC)))
            .orderBy(capsule.createdAt.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, publicCapsules);
    }
}
