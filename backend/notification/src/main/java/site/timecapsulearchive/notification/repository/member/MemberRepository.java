package site.timecapsulearchive.notification.repository.member;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.notification.global.aop.Trace;
import site.timecapsulearchive.notification.service.dto.MemberNotificationInfoDto;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Trace
    public MemberNotificationInfoDto findFCMToken(Long memberId) {
        final String sql = """
            SELECT m.fcm_token, m.notification_enabled 
            FROM member m 
            WHERE m.member_id = :memberId and m.deleted_at is null
            """;

        final SqlParameterSource parameters = new MapSqlParameterSource("memberId", memberId);

        return namedParameterJdbcTemplate.queryForObject(
            sql,
            parameters,
            (rs, rowNum) -> new MemberNotificationInfoDto(rs.getString("fcm_token"),
                rs.getBoolean("notification_enabled"))
        );
    }

    @Trace
    public List<MemberNotificationInfoDto> findFCMTokens(List<Long> memberIds) {
        final String sql = """
            SELECT m.fcm_token, m.notification_enabled
            FROM member m 
            WHERE m.member_id IN (:memberIds) and m.deleted_at is null
            """;

        final SqlParameterSource parameters = new MapSqlParameterSource("memberIds", memberIds);

        return namedParameterJdbcTemplate.query(
            sql,
            parameters,
            (rs, rowNum) -> new MemberNotificationInfoDto(rs.getString("fcm_token"),
                rs.getBoolean("notification_enabled"))
        );
    }
}
