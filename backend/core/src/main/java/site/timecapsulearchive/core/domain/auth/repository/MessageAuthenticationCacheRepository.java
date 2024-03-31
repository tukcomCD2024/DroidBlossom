package site.timecapsulearchive.core.domain.auth.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageAuthenticationCacheRepository {

    private static final int MINUTE = 5;
    private static final String PREFIX = "messageAuthentication:";

    private final StringRedisTemplate redisTemplate;

    public void save(final Long memberId, final String receiver, final String code) {
        redisTemplate.opsForHash().put(PREFIX + memberId, receiver, code);
        redisTemplate.expire(PREFIX + memberId, MINUTE, TimeUnit.MINUTES);
    }

    public Optional<String> findMessageAuthenticationCodeByMemberId(final Long memberId,
        final String receiver) {
        String code = (String) redisTemplate.opsForHash().get(PREFIX + memberId, receiver);

        if (code == null) {
            return Optional.empty();
        }

        return Optional.of(code);
    }
}
