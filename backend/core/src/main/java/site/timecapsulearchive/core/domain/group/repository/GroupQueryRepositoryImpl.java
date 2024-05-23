package site.timecapsulearchive.core.domain.group.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static site.timecapsulearchive.core.domain.capsule.entity.QCapsule.capsule;
import static site.timecapsulearchive.core.domain.friend.entity.QMemberFriend.memberFriend;
import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupSummaryDto;

@Repository
@RequiredArgsConstructor
public class GroupQueryRepositoryImpl implements GroupQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<GroupSummaryDto> findGroupsSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<GroupSummaryDto> groups = jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupSummaryDto.class,
                    group.id,
                    group.groupName,
                    group.groupDescription,
                    group.groupProfileUrl,
                    group.createdAt,
                    memberGroup.isOwner
                )
            )
            .from(memberGroup)
            .join(memberGroup.group, group)
            .where(memberGroup.member.id.eq(memberId).and(memberGroup.createdAt.lt(createdAt)))
            .limit(size + 1)
            .fetch();

        final boolean hasNext = groups.size() > size;
        if (hasNext) {
            groups.remove(size);
        }

        return new SliceImpl<>(groups, Pageable.ofSize(size), groups.size() > size);
    }

    public Optional<GroupDetailDto> findGroupDetailByGroupIdAndMemberId(final Long groupId,
        final Long memberId) {
        return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(group)
                .join(memberGroup).on(memberGroup.group.id.eq(groupId))
                .join(member).on(member.id.eq(memberGroup.member.id))
                .where(member.id.ne(memberId))
                .transform(
                    groupBy(group.id).as(
                        Projections.constructor(
                            GroupDetailDto.class,
                            group.groupName,
                            group.groupDescription,
                            group.groupProfileUrl,
                            group.createdAt,
                            list(
                                Projections.constructor(
                                    GroupMemberDto.class,
                                    member.id,
                                    member.profileUrl,
                                    member.nickname,
                                    member.tag,
                                    memberGroup.isOwner
                                )
                            )
                        )
                    )
                )
                .get(groupId)
        );
    }

    public Long findGroupCapsuleCount(final Long groupId) {
        return jpaQueryFactory.select(capsule.count())
            .from(capsule)
            .where(capsule.group.id.eq(groupId))
            .fetchOne();
    }

    public Boolean findGroupEditPermission(final Long groupId, final Long memberId) {
        return jpaQueryFactory.select(memberGroup.isOwner)
            .from(memberGroup)
            .where(memberGroup.group.id.eq(groupId).and(memberGroup.member.id.eq(memberId)))
            .fetchOne();
    }

    public List<Long> findFriendIds(final List<Long> groupMemberIds, final Long memberId) {
        return jpaQueryFactory.select(memberFriend.friend.id)
            .from(memberFriend)
            .where(
                memberFriend.friend.id.in(groupMemberIds).and(memberFriend.owner.id.eq(memberId)))
            .fetch();
    }
}
