package site.timecapsulearchive.core.domain.capsule.group_capsule.repository;

import static site.timecapsulearchive.core.domain.capsule.entity.QGroupCapsuleOpen.groupCapsuleOpen;
import static site.timecapsulearchive.core.domain.member.entity.QMember.member;
import static site.timecapsulearchive.core.domain.member_group.entity.QMemberGroup.memberGroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Capsule;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;

@Repository
@RequiredArgsConstructor
public class GroupCapsuleOpenQueryRepositoryImpl implements GroupCapsuleOpenQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    public void bulkSave(
        final Long groupId,
        final List<Long> groupMemberIds,
        final Capsule capsule
    ) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO group_capsule_open (
                group_capsule_open_id, is_opened, member_id, capsule_id, group_id, created_at, updated_at
                ) values (?, ? ,? ,? ,?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    boolean isOpened = capsule.getDueDate() == null;

                    ps.setNull(1, Types.BIGINT);
                    ps.setBoolean(2, isOpened);
                    ps.setLong(3, groupMemberIds.get(i));
                    ps.setLong(4, capsule.getId());
                    ps.setLong(5, groupId);
                    ps.setTimestamp(6, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(7, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                }

                @Override
                public int getBatchSize() {
                    return groupMemberIds.size();
                }
            }
        );
    }

    public List<GroupCapsuleMemberDto> findGroupCapsuleMembers(
        final Long capsuleId,
        final Long groupId
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
            .from(groupCapsuleOpen)
            .join(memberGroup).on(groupCapsuleOpen.member.id.eq(memberGroup.member.id))
            .join(groupCapsuleOpen.member, member)
            .where(groupCapsuleOpen.group.id.eq(groupId)
                .and(groupCapsuleOpen.capsule.id.eq(capsuleId))
                .and(memberGroup.group.id.eq(groupId))
            )
            .fetch();
    }
}
