package site.timecapsulearchive.core.domain.capsule.generic_capsule.repository.image;

import static site.timecapsulearchive.core.domain.capsule.entity.QImage.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.capsule.entity.Image;

@Repository
@RequiredArgsConstructor
public class ImageQueryRepositoryImpl implements ImageQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory jpaQueryFactory;

    public void bulkSave(final List<Image> images) {
        jdbcTemplate.batchUpdate(
            """
                INSERT INTO image (
                image_id, image_url, member_id, capsule_id, created_at, updated_at
                ) values (?, ?, ?, ?, ?, ?)
                """,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                    final Image image = images.get(i);
                    ps.setNull(1, Types.BIGINT);
                    ps.setString(2, image.getImageUrl());
                    ps.setLong(3, image.getMember().getId());
                    ps.setLong(4, image.getCapsule().getId());
                    ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                    ps.setTimestamp(6, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
                }

                @Override
                public int getBatchSize() {
                    return images.size();
                }
            }
        );
    }

    public Optional<Long> deleteImage(final Long imageId) {
        return Optional.of(jpaQueryFactory
            .delete(image)
            .where(image.id.eq(imageId))
            .execute()
        );
    }

}
