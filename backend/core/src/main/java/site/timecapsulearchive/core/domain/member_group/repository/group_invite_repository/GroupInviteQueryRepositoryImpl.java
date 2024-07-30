package site.timecapsulearchive.core.domain.member_group.repository.group_invite_repository;


import static site.timecapsulearchive.core.domain.group.entity.QGroup.group;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QGroupInvite.groupInvite;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupInviteSummaryDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInviteMemberDto;
import site.timecapsulearchive.core.domain.member_group.data.dto.GroupSendingInvitesSliceRequestDto;
import site.timecapsulearchive.core.global.util.SliceUtil;

@Repository
@RequiredArgsConstructor
public class GroupInviteQueryRepositoryImpl implements GroupInviteQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void bulkSave(
        final Long groupOwnerId,
        final Long groupId,
        final List<Long> groupMemberIds
    ) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO group_invite (
                group_invite_id, group_owner_id, group_member_id, group_id, created_at, updated_at
                ) values (?, ?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Long groupMember = groupMemberIds.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setLong(2, groupOwnerId);
                    ps.setLong(3, groupMember);
                    ps.setLong(4, groupId);
                    ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(6, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                }

                @Override
                public int getBatchSize() {
                    return groupMemberIds.size();
                }
            }
        );
    }

    @Override
    public List<Long> findGroupInviteIdsByGroupIdAndGroupOwnerId(
        final Long groupId,
        final Long memberId
    ) {
        return jpaQueryFactory
            .select(groupInvite.id)
            .from(groupInvite)
            .where(groupInvite.group.id.eq(groupId).and(groupInvite.groupOwner.id.eq(memberId)))
            .fetch();
    }

    @Override
    public Slice<GroupInviteSummaryDto> findGroupReceivingInvitesSlice(
        final Long memberId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        final List<GroupInviteSummaryDto> groupInviteSummaryDtos = jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupInviteSummaryDto.class,
                    groupInvite.id,
                    group.id,
                    group.groupName,
                    group.groupProfileUrl,
                    group.groupDescription,
                    groupInvite.createdAt,
                    member.nickname
                )
            )
            .from(groupInvite)
            .join(groupInvite.group, group)
            .join(groupInvite.groupOwner, member)
            .where(groupInvite.groupMember.id.eq(memberId).and(groupInvite.createdAt.lt(createdAt)))
            .orderBy(groupInvite.id.desc())
            .limit(size + 1)
            .fetch();

        return SliceUtil.makeSlice(size, groupInviteSummaryDtos);
    }

    @Override
    public List<Long> findGroupMemberIdsByGroupIdAndGroupOwnerId(
        final Long groupId,
        final Long groupOwnerId
    ) {
        return jpaQueryFactory
            .select(groupInvite.groupMember.id)
            .from(groupInvite)
            .where(groupInvite.groupOwner.id.eq(groupOwnerId).and(groupInvite.group.id.eq(groupId)))
            .fetch();
    }

    @Override
    public Slice<GroupSendingInviteMemberDto> findGroupSendingInvites(
        final GroupSendingInvitesSliceRequestDto dto
    ) {
        List<GroupSendingInviteMemberDto> groupSendingInviteMemberDtos = jpaQueryFactory
            .select(
                Projections.constructor(
                    GroupSendingInviteMemberDto.class,
                    groupInvite.id,
                    member.id,
                    member.nickname,
                    member.profileUrl,
                    groupInvite.createdAt
                )
            )
            .from(groupInvite)
            .join(groupInvite.groupMember, member)
            .where(
                groupInviteIdPagingCursorCondition(dto),
                groupInvite.group.id.eq(dto.groupId())
                    .and(groupInvite.groupOwner.id.eq(dto.memberId()))
            )
            .orderBy(groupInvite.id.desc())
            .limit(dto.size() + 1)
            .fetch();

        return SliceUtil.makeSlice(dto.size(), groupSendingInviteMemberDtos);
    }

    private BooleanExpression groupInviteIdPagingCursorCondition(
        GroupSendingInvitesSliceRequestDto dto) {
        if (dto.groupInviteId() == null) {
            return null;
        }

        return groupInvite.id.lt(dto.groupInviteId());
    }
}
