package site.timecapsulearchive.core.domain.friend.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendInviteQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkSave(final Long ownerId, final List<Long> friendIds) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO friend_invite (
                friend_invite_id, owner_id, friend_id
                ) values (?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Long friendId = friendIds.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setLong(2, ownerId);
                    ps.setLong(3, friendId);
                }

                @Override
                public int getBatchSize() {
                    return friendIds.size();
                }
            }
        );
    }
}
