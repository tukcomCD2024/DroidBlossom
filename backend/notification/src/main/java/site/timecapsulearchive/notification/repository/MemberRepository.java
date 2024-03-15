package site.timecapsulearchive.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public String findFCMToken(Long memberId) {
        String sql = "SELECT m.fcm_token FROM member m WHERE m.member_id = ?";

        return jdbcTemplate.queryForObject(
            sql,
            (rs, rowNum) -> rs.getString("fcm_token"),
            memberId
        );
    }
}
