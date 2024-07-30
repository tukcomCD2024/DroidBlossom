package site.timecapsulearchive.core.domain.capsule.secret_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;
import static site.timecapsulearchive.core.domain.capsule.entity.QVideo.video;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
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
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class SecretCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<CapsuleSummaryDto> findSecretCapsuleSummaryDtosByMemberIdAndCapsuleId(
        final Long memberId,
        final Long capsuleId
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
                        capsule.point,
                        capsule.address.fullRoadAddressName,
                        capsule.address.roadName,
                        capsule.isOpened,
                        capsule.createdAt
                    )
                )
                .from(capsule)
                .where(capsule.id.eq(capsuleId).and(capsule.member.id.eq(memberId))
                    .and(capsule.type.eq(CapsuleType.SECRET)))
                .fetchOne()
        );
    }

    public Optional<CapsuleDetailDto> findSecretCapsuleDetailDtosByMemberIdAndCapsuleId(
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
            .join(capsuleSkin)
            .on(capsule.capsuleSkin.eq(capsuleSkin), capsuleSkin.deletedAt.isNull())
            .join(member).on(capsule.member.eq(member), member.deletedAt.isNull())
            .leftJoin(image).on(capsule.eq(image.capsule), image.deletedAt.isNull())
            .leftJoin(video).on(capsule.eq(video.capsule), video.deletedAt.isNull())
            .where(
                capsule.id.eq(capsuleId)
                    .and(capsule.member.id.eq(memberId))
                    .and(capsule.type.eq(CapsuleType.SECRET))
            )
            .fetchFirst();

        if (detailDto.capsuleId() == null) {
            return Optional.empty();
        }
        return Optional.of(detailDto);
    }

    private StringExpression groupConcatDistinct(final StringExpression expression) {
        return Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", expression);
    }

    public Slice<CapsuleBasicInfoDto> findSecretCapsuleSliceByMemberIdAndCreatedAt(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<CapsuleBasicInfoDto> mySecretCapsules = jpaQueryFactory
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
            .where(
                capsule.member.id.eq(memberId),
                capsule.createdAt.lt(createdAt),
                capsule.type.eq(CapsuleType.SECRET)
            )
            .orderBy(capsule.id.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, mySecretCapsules);
    }
}