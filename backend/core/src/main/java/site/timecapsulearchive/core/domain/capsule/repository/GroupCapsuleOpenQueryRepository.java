package site.timecapsulearchive.core.domain.capsule.repository;

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

@Repository
@RequiredArgsConstructor
public class GroupCapsuleOpenQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkSave(final List<Long> groupMemberIds, final Capsule capsule) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO group_capsule_open (
                group_capsule_open_id, is_opened, member_id, capsule_id, created_at, updated_at
                ) values (?, ? ,? ,? ,?, ?)
                """,
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setNull(1, Types.BIGINT);
                    ps.setBoolean(2, false);
                    ps.setLong(3, groupMemberIds.get(i));
                    ps.setLong(4, capsule.getId());
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
}
