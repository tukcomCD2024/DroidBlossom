package site.timecapsulearchive.core.domain.auth.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import site.timecapsulearchive.core.domain.auth.service.MemberInfo;

@Repository
@RequiredArgsConstructor
public class MemberInfoCacheRepository {

    private static final int MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY = 30;

    private final RedisTemplate<String, MemberInfo> redisTemplate;

    public void save(String key, MemberInfo memberProfile) {
        redisTemplate.opsForValue().set(
            key,
            memberProfile,
            MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY,
            TimeUnit.DAYS
        );
    }

    public Optional<MemberInfo> getMemberProfile(String profileKey) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(profileKey));
    }

    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }
}
