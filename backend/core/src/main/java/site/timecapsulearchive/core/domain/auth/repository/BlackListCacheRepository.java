package site.timecapsulearchive.core.domain.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlackListCacheRepository {

    private static final String PREFIX = "blackList_memberId_accessToken:";

    private final StringRedisTemplate redisTemplate;

    public void save(final Long memberId, final String accessToken, long leftTime) {
        redisTemplate.opsForValue()
            .set(PREFIX + memberId, accessToken, leftTime, TimeUnit.MILLISECONDS);
    }

    public boolean exist(Long memberId) {
        String accessToken = redisTemplate.opsForValue()
            .get(PREFIX + memberId);

        return accessToken != null;
    }
}
