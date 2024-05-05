package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.capsuleskin.entity.QCapsuleSkin.capsuleSkin;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.MyGroupCapsuleDto;

@Repository
@RequiredArgsConstructor
public class GroupCapsuleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<MyGroupCapsuleDto> findMyGroupCapsuleSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<MyGroupCapsuleDto> groupCapsules = jpaQueryFactory
            .select(
                Projections.constructor(
                    MyGroupCapsuleDto.class,
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

        final boolean hasNext = groupCapsules.size() > size;
        if (hasNext) {
            groupCapsules.remove(size);
        }

        return new SliceImpl<>(groupCapsules, Pageable.ofSize(size), hasNext);
    }
}
