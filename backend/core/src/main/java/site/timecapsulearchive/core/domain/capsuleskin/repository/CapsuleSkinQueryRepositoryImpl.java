package site.timecapsulearchive.core.domain.capsuleskin.repository;

import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinSummaryDto;
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class CapsuleSkinQueryRepositoryImpl implements CapsuleSkinQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<CapsuleSkinSummaryDto> findCapsuleSkinSliceByCreatedAtAndMemberId(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<CapsuleSkinSummaryDto> capsuleSkins = jpaQueryFactory
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

        return SliceUtil.makeSlice(size, capsuleSkins);
    }

    @Override
    public boolean existByImageUrlAndMemberId(String imageUrl, Long memberId) {
        Integer count = jpaQueryFactory
            .selectOne()
            .from(capsuleSkin)
            .where(capsuleSkin.imageUrl.eq(imageUrl).and(capsuleSkin.member.id.eq(memberId)))
            .fetchOne();

        return count != null;
    }
}
