package site.timecapsulearchive.core.domain.group_member.repository.memberGroupRepository;

import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.group_member.entity.QMemberGroup.memberGroup;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.group_member.data.GroupOwnerSummaryDto;

@Repository
@RequiredArgsConstructor
public class MemberGroupQueryRepositoryImpl implements MemberGroupQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<GroupOwnerSummaryDto> findOwnerInMemberGroup(final Long groupId,
        final Long memberId) {
        return Optional.ofNullable(jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupOwnerSummaryDto.class,
                    member.nickname,
                    memberGroup.isOwner,
                    group.groupProfileUrl
                )
            )
            .from(memberGroup)
            .join(memberGroup.member, member)
            .join(memberGroup.group, group)
            .where(memberGroup.group.id.eq(groupId)
                .and(memberGroup.member.id.eq(memberId)))
            .fetchFirst()
        );
    }

    @Override
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
