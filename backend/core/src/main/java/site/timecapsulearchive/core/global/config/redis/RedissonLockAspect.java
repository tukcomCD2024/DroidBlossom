package site.timecapsulearchive.core.global.config.redis;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.RedisLockException;
import site.timecapsulearchive.core.global.util.RedisLockSpELParser;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(RedissonLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String lockKey =
            method.getName() + ":" + RedisLockSpELParser.getLockKey(signature.getParameterNames(),
                joinPoint.getArgs(), redissonLock.value());

        long waitTime = redissonLock.waitTime();
        long leaseTime = redissonLock.leaseTime();

        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(waitTime, leaseTime, MILLISECONDS);
            if (isLocked) {
                log.info("락을 얻는데 성공하였습니다. (락 키 : {})", lockKey);
                return joinPoint.proceed();
            } else {
                log.warn("락을 얻는데 실패하였습니다. (락 키 : {})", lockKey);
                throw new RedisLockException(ErrorCode.REDIS_FAILED_GET_LOCK_ERROR);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("락을 얻는데 인터럽트가 발생하였습니다. (락 키 : {})", lockKey);
            throw new RedisLockException(ErrorCode.REDIS_INTERRUPT_ERROR);
        } finally {
            if (isLocked) {
                lock.unlock();
                log.info("락을 해제하는데 성공하였습니다. (락 키 : {})", lockKey);
            }
        }
    }

}
