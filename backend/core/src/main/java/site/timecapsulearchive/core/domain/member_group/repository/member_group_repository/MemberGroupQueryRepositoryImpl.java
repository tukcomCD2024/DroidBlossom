package site.timecapsulearchive.core.domain.member_group.repository.member_group_repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QGroupCapsuleOpen.groupCapsuleOpen;
import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;
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

    @Override
    public List<Long> findGroupMemberIdsByGroupId(final Long groupId) {
        return jpaQueryFactory
            .select(memberGroup.member.id)
            .from(memberGroup)
            .where(memberGroup.group.id.eq(groupId))
            .fetch();
    }

    @Override
    public boolean existMemberGroupByMemberIdAndGroupId(Long memberId, Long groupId) {
        final Integer count = jpaQueryFactory.selectOne()
            .from(memberGroup)
            .where(memberGroup.member.id.eq(memberId).and(memberGroup.group.id.eq(groupId)))
            .fetchFirst();

        return count != null;
    }

    @Override
    public List<GroupCapsuleMemberDto> findGroupCapsuleMembers(
        final Long groupId,
        final Long capsuleId
    ) {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupCapsuleMemberDto.class,
                    member.id,
                    member.nickname,
                    member.profileUrl,
                    memberGroup.isOwner,
                    groupCapsuleOpen.isOpened
                )
            )
            .from(memberGroup)
            .join(memberGroup.member, member)
            .join(groupCapsuleOpen).on(memberGroup.member.id.eq(groupCapsuleOpen.member.id))
            .where(memberGroup.group.id.eq(groupId).and(groupCapsuleOpen.capsule.id.eq(capsuleId)))
            .fetch();
    }

    @Override
    public List<Long> findGroupIdsByMemberId(Long memberId) {
        return jpaQueryFactory
            .select(
                memberGroup.group.id
            )
            .from(memberGroup)
            .where(memberGroup.member.id.eq(memberId))
            .fetch();
    }
}
