package site.timecapsulearchive.core.domain.capsule.public_capsule.repository;

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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.data.dto.CapsuleBasicInfoDto;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto.PublicCapsuleDetailDto;

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
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .leftJoin(memberFriend).on(capsule.member.id.eq(memberFriend.owner.id)
                .or(capsule.member.id.eq(memberFriend.friend.id)))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC))
                .and(capsule.member.id.eq(memberId).or(memberFriend.friend.id.eq(memberId))))
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
        final Long memberId,
        final Long capsuleId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    Projections.constructor(
                        CapsuleSummaryDto.class,
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
                .join(member).on(capsule.member.id.eq(member.id))
                .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
                .leftJoin(memberFriend).on(capsule.member.id.eq(memberFriend.owner.id)
                    .or(capsule.member.id.eq(memberFriend.friend.id)))
                .where(capsule.id.eq(capsuleId).and(capsule.type.eq(CapsuleType.PUBLIC))
                    .and(capsule.member.id.eq(memberId).or(memberFriend.friend.id.eq(memberId))))
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
                    capsule.type
                )
            )
            .from(capsule)
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .leftJoin(image).on(capsule.id.eq(image.capsule.id))
            .leftJoin(video).on(capsule.id.eq(video.capsule.id))
            .where(capsule.member.id.in(memberIds)
                .and(capsule.createdAt.lt(createdAt))
                .and(capsule.type.eq(CapsuleType.PUBLIC)))
            .groupBy(capsule.id)
            .orderBy(capsule.createdAt.desc())
            .limit(size + 1)
            .fetch();

        final boolean hasNext = canMoreRead(size, publicCapsuleDetailDtos.size());
        if (hasNext) {
            publicCapsuleDetailDtos.remove(size);
        }

        return new SliceImpl<>(publicCapsuleDetailDtos, Pageable.ofSize(size), hasNext);

    }

    private boolean canMoreRead(final int size, final int capsuleSize) {
        return capsuleSize > size;
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
            .join(member).on(capsule.member.id.eq(member.id))
            .join(capsuleSkin).on(capsule.capsuleSkin.id.eq(capsuleSkin.id))
            .where(capsule.member.id.eq(memberId)
                .and(capsule.createdAt.lt(createdAt))
                .and(capsule.type.eq(CapsuleType.PUBLIC)))
            .orderBy(capsule.createdAt.desc())
            .limit(size + 1)
            .fetch();

        final boolean hasNext = publicCapsules.size() > size;
        if (hasNext) {
            publicCapsules.remove(size);
        }

        return new SliceImpl<>(publicCapsules, Pageable.ofSize(size), hasNext);
    }
}
