package site.timecapsulearchive.core.domain.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.auth.exception.NotFoundCertificationNumberException;

@Repository
@RequiredArgsConstructor
public class MessageAuthenticationCacheRepository {

    private static final int MINUTE = 5;
    private static final String PREFIX = "messageAuthentication:";

    private final StringRedisTemplate redisTemplate;

    public void save(final Long memberId, final String code) {
        redisTemplate.opsForValue().set(PREFIX + memberId, code, MINUTE, TimeUnit.MINUTES);
    }

    public Integer get(final Long memberId) {
        String findCertificationNumber = redisTemplate.opsForValue().get(PREFIX + memberId);

        if (findCertificationNumber == null) {
            throw new NotFoundCertificationNumberException();
        }

        return Integer.parseInt(findCertificationNumber);
    }
}
