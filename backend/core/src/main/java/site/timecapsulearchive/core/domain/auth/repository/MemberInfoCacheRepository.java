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
    private static final String PREFIX = "memberInfo:";

    private final RedisTemplate<String, MemberInfo> redisTemplate;

    /**
     * 키와 {@code memberInfo}를 받아서 캐시에 저장
     * @param key 캐시에 저장할 키, UUID string
     * @param memberInfo 저장할 사용자 정보
     */
    public void save(String key, MemberInfo memberInfo) {
        redisTemplate.opsForValue().set(
            PREFIX + key,
            memberInfo,
            MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY,
            TimeUnit.DAYS
        );
    }

    /**
     * 사용자 정보 조회
     * @param infoKey 캐시에 저장된 키, UUID string
     * @return {@code Optional<MemberInfo>} 사용자 정보
     */
    public Optional<MemberInfo> getMemberInfo(String infoKey) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + infoKey));
    }

    /**
     * 삭제 로직 대신 키의 이름을 변경, O(1) 소요
     * @param oldKey 이전 키
     * @param newKey 새로운 키
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(PREFIX + oldKey, PREFIX + newKey);
    }
}
