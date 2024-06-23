package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.video;

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
import site.timecapsulearchive.core.domain.capsule.entity.Video;

@Repository
@RequiredArgsConstructor
public class VideoQueryRepositoryImpl implements VideoQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkSave(final List<Video> videos) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO video (
                video_id, video_url, member_id, capsule_id, created_at, updated_at, is_deleted
                ) values (?, ?, ?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Video video = videos.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setString(2, video.getVideoUrl());
                    ps.setLong(3, video.getMember().getId());
                    ps.setLong(4, video.getCapsule().getId());
                    ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(6, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setBoolean(7, Boolean.FALSE);
                }

                @Override
                public int getBatchSize() {
                    return videos.size();
                }
            }
        );
    }
}
