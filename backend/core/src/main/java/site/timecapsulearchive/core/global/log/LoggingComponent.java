package site.timecapsulearchive.core.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingComponent {

    @Around("execution(public * site.timecapsulearchive.core.domain.*.service.*.*(..))")
    public Object logServiceLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Service");
    }

    @Around("""
        execution(public * site.timecapsulearchive.core.domain.*.repository.*.*(..))
        && (@target(org.springframework.stereotype.Repository) ||
        target(org.springframework.data.repository.Repository+))
        """)
    public Object logRepositoryLayer(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Repository");
    }

    @Around("execution(public * site.timecapsulearchive.core.infra.*.manager.*.*(..))")
    public Object logExternalApi(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "External API");
    }

    @Around("execution(public * site.timecapsulearchive.core.global.api.limit.ApiLimitCheckInterceptor.*(..))")
    public Object logApiLimitCheckInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "API Limit Check");
    }

    private Object logMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("{} Layer - Method {} - Start", layer, methodName);
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        log.info("{} Layer - Method {} - End, Execution Time: {}ms", layer, methodName, (endTime - startTime));

        return result;
    }
}