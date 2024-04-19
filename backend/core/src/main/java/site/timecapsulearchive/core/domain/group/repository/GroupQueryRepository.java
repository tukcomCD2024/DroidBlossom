package site.timecapsulearchive.core.domain.group.repository;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.group.entity.QMemberGroup.memberGroup;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.group.data.dto.GroupDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;

@Repository
@RequiredArgsConstructor
public class GroupQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<GroupDetailDto> findGroupDetailByGroupId(final Long groupId) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(
                    group.groupName,
                    group.groupDescription,
                    group.groupProfileUrl,
                    group.createdAt,
                    member.id,
                    member.profileUrl,
                    member.nickname,
                    member.tag,
                    memberGroup.isOwner
                )
                .from(group)
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
                            list(
                                Projections.constructor(
                                    GroupMemberSummaryDto.class,
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
}
