package site.timecapsulearchive.notification.repository.member;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public String findFCMToken(Long memberId) {
        String sql = "SELECT m.fcm_token FROM member m WHERE m.member_id = ? and m.deleted_at is null";

        return jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> rs.getString("fcm_token"),
            memberId
        );
    }

    public List<String> findFCMTokens(List<Long> memberIds) {
        final String sql = "SELECT m.fcm_token FROM member m WHERE m.member_id IN (:memberIds) and m.deleted_at is null";

        final SqlParameterSource parameters = new MapSqlParameterSource("memberIds", memberIds);

        return namedParameterJdbcTemplate.query(
            sql,
            parameters,
            (rs, rowNum) -> rs.getString("fcm_token")
        );
    }
}
