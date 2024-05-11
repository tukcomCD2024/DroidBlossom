package site.timecapsulearchive.core.domain.group.repository;

import static site.timecapsulearchive.core.domain.group.entity.QMemberGroup.memberGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberGroupQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Boolean> findIsOwnerByMemberIdAndGroupId(
        final Long memberId,
        final Long groupId
    ) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(memberGroup.isOwner)
                .from(memberGroup)
                .where(memberGroup.member.id.eq(memberId).and(memberGroup.group.id.eq(groupId)))
                .fetchOne()
        );
    }
}
