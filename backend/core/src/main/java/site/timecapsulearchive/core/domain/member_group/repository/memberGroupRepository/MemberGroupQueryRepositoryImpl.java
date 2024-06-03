package site.timecapsulearchive.core.domain.member_group.repository.memberGroupRepository;

import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupOwnerSummaryDto;

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

    @Override
    public Optional<List<Long>> findGroupMemberIds(final Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
            .select(memberGroup.member.id)
            .from(memberGroup)
            .where(memberGroup.group.id.eq(groupId))
            .fetch());
    }

    @Override
    public Optional<Long> findGroupOwnerId(final Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
            .select(memberGroup.member.id)
            .from(memberGroup)
            .where(memberGroup.group.id.eq(groupId).and(memberGroup.isOwner.eq(true)))
            .fetchOne());
    }

    @Override
    public List<GroupMemberDto> findGroupMemberInfos(
        final Long memberId,
        final Long groupId
    ) {
        return jpaQueryFactory
            .select(Projections.constructor(
                    GroupMemberDto.class,
                    member.id,
                    member.profileUrl,
                    member.nickname,
                    member.tag,
                    memberGroup.isOwner
                )
            )
            .from(memberGroup)
            .join(memberGroup.member, member)
            .where(memberGroup.group.id.eq(groupId))
            .fetch();
    }

    @Override
    public Optional<Long> findGroupMembersCount(final Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
            .select(memberGroup.count())
            .from(memberGroup)
            .where(memberGroup.group.id.eq(groupId))
            .fetchOne()
        );
    }
}
