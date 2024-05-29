package site.timecapsulearchive.core.domain.group.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    /**
     * 사용자를 제외한 그룹원 정보와 그룹의 상세정보를 반환한다.
     * @param groupId 상세정보를 찾을 그룹 아이디
     * @param memberId 사용자 아이디
     * @return 그룹의 상세정보({@code memberId} 제외 그룹원)
     */
    public Optional<GroupDetailDto> findGroupDetailByGroupIdAndMemberId(final Long groupId,
        final Long memberId) {
        GroupDetailDto groupDetailDtoIncludeMe =
            jpaQueryFactory
                .selectFrom(group)
                .join(memberGroup).on(memberGroup.group.id.eq(group.id))
                .join(member).on(member.id.eq(memberGroup.member.id))
                .where(group.id.eq(groupId))
                .transform(
                    groupBy(group.id).as(
                        Projections.constructor(
                            GroupDetailDto.class,
                            group.groupName,
                            group.groupDescription,
                            group.groupProfileUrl,
                            group.createdAt,
                            Expressions.asBoolean(Boolean.FALSE),
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
                .get(groupId);

        if (Objects.isNull(groupDetailDtoIncludeMe)) {
            return Optional.empty();
        }

        boolean isOwner = false;
        List<GroupMemberDto> groupMemberDtosExcludeMe = new ArrayList<>();
        for (GroupMemberDto dto : groupDetailDtoIncludeMe.members()) {
            if (!dto.memberId().equals(memberId)) {
                groupMemberDtosExcludeMe.add(dto);
            } else {
                isOwner = dto.isOwner();
            }
        }

        return Optional.of(
            GroupDetailDto.as(
                groupDetailDtoIncludeMe.groupName(),
                groupDetailDtoIncludeMe.groupDescription(),
                groupDetailDtoIncludeMe.groupProfileUrl(),
                groupDetailDtoIncludeMe.createdAt(),
                isOwner,
                groupMemberDtosExcludeMe
            )
        );
    }
}
