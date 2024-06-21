package site.timecapsulearchive.core.domain.auth.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenCacheRepository {

    private static final int MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY = 30;
    private static final String PREFIX = "reIssue_memberId_refreshToken:";

    private final StringRedisTemplate redisTemplate;

    public void save(
        final Long memberId,
        final String refreshToken
    ) {
        redisTemplate.opsForValue().set(
            PREFIX + memberId,
            refreshToken,
            MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY,
            TimeUnit.DAYS
        );
    }

    public Optional<String> findRefreshTokenByMemberId(final Long memberId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + memberId));
    }
}
