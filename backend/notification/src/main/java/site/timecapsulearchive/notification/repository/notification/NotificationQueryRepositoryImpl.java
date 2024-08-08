package site.timecapsulearchive.notification.repository.notification;

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
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationStatus;
import site.timecapsulearchive.notification.global.aop.Trace;

@Repository
@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository{

    private final JdbcTemplate jdbcTemplate;

    @Trace
    public void bulkSave(List<Notification> notifications) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO notification (
                    notification_id,
                    title,
                    text,
                    member_id,
                    notification_category_id,
                    image_url,
                    created_at,
                    updated_at,
                    status,
                    deleted_at
                ) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Notification notification = notifications.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setString(2, notification.getTitle());
                    ps.setString(3, notification.getText());
                    ps.setLong(4, notification.getMemberId());
                    ps.setLong(5, notification.getNotificationCategory().getId());
                    ps.setString(6, notification.getImageUrl());
                    ps.setTimestamp(7, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(8, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setString(9, String.valueOf(NotificationStatus.SUCCESS));
                    ps.setNull(10, Types.TIMESTAMP);
                }

                @Override
                public int getBatchSize() {
                    return notifications.size();
                }
            }
        );
    }
}