package site.timecapsulearchive.core.domain.capsuleskin.repository;

import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsuleskin.dto.CapsuleSkinSummaryDto;

@Repository
@RequiredArgsConstructor
public class CapsuleSkinQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<CapsuleSkinSummaryDto> findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<CapsuleSkinSummaryDto> capsuleSkins = selectCapsuleSkinSummaryResponse(
            memberId,
            size,
            createdAt
        );

        final boolean hasNext = canMoreRead(size, capsuleSkins.size());
        if (hasNext) {
            capsuleSkins.remove(size);
        }

        return new SliceImpl<>(capsuleSkins, Pageable.ofSize(size), hasNext);
    }

    private List<CapsuleSkinSummaryDto> selectCapsuleSkinSummaryResponse(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    CapsuleSkinSummaryDto.class,
                    capsuleSkin.id,
                    capsuleSkin.imageUrl,
                    capsuleSkin.skinName,
                    capsuleSkin.createdAt
                )
            )
            .from(capsuleSkin)
            .where(capsuleSkin.member.id.eq(memberId).and(capsuleSkin.createdAt.lt(createdAt)))
            .orderBy(capsuleSkin.id.desc())
            .limit(size + 1)
            .fetch();
    }

    private boolean canMoreRead(
        final int size,
        final int capsuleSize
    ) {
        return capsuleSize > size;
    }
}
