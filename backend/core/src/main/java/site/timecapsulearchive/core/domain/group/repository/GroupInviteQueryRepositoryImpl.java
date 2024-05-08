package site.timecapsulearchive.core.domain.group.repository;

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

    @Override
    public void bulkSave(Long groupOwnerId, List<Long> groupMemberIds) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO group_invite (
                group_invite_id, group_owner_id, group_member_id, created_at, updated_at
                ) values (?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Long groupMember = groupMemberIds.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setLong(2, groupOwnerId);
                    ps.setLong(3, groupMember);
                    ps.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                }

                @Override
                public int getBatchSize() {
                    return groupMemberIds.size();
                }
            }
        );
    }

}
