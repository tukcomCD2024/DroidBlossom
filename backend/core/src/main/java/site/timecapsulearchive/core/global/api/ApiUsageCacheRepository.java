package site.timecapsulearchive.core.global.api;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApiUsageCacheRepository {

    private static final String PREFIX = "apiUsage:";
    private static final String SMS_API_USAGE = "smsApi";
    private static final String FIRST_REQUEST = "1";
    private static final int EXPIRATION_DAYS = 1;

    private final StringRedisTemplate redisTemplate;

    public Optional<Integer> getSmsApiUsage(Long memberId) {
        String result = (String) redisTemplate.opsForHash().get(PREFIX + memberId, SMS_API_USAGE);

        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(result));
    }

    public void increaseSmsApiUsage(Long memberId) {
        redisTemplate.opsForHash().increment(PREFIX + memberId, SMS_API_USAGE, 1);
    }

    public void saveAsFirstRequest(Long memberId) {
        String key = PREFIX + memberId;

        redisTemplate.opsForHash().put(key, SMS_API_USAGE, FIRST_REQUEST);

        redisTemplate.expire(key, EXPIRATION_DAYS, TimeUnit.DAYS);
    }
}
