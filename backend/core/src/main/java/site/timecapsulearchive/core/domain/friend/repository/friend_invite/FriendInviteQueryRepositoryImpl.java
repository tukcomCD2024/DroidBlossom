package site.timecapsulearchive.core.domain.friend.repository.friend_invite;

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
public class FriendInviteQueryRepositoryImpl implements FriendInviteQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkSave(final Long ownerId, final List<Long> friendIds) {
        if (friendIds.isEmpty()) {
            return;
        }

        jdbcTemplate.batchUpdate(
            """
                INSERT INTO friend_invite (
                friend_invite_id, owner_id, friend_id, created_at, updated_at
                ) values (?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Long friendId = friendIds.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setLong(2, ownerId);
                    ps.setLong(3, friendId);
                    ps.setTimestamp(4, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                }

                @Override
                public int getBatchSize() {
                    return friendIds.size();
                }
            }
        );
    }
}
