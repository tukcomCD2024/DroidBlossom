package site.timecapsulearchive.core.domain.member_group.repository.groupInviteRepository;


import static site.timecapsulearchive.core.domain.member_group.entity.QGroupInvite.groupInvite;

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

@Repository
@RequiredArgsConstructor
public class GroupInviteQueryRepositoryImpl implements GroupInviteQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void bulkSave(final Long groupOwnerId, final Long groupId, final List<Long> groupMemberIds) {
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
}
