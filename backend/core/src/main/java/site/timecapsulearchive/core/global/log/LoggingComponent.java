package site.timecapsulearchive.core.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingComponent {

    @Before("execution(* site.timecapsulearchive..*(..)) "
        + "&& !within(site.timecapsulearchive.core.infra..config..*)"
        + "&& !within(site.timecapsulearchive.core.domain..api..*)"
        + "&& !within(site.timecapsulearchive.core.global..*)")
    public void doTraceBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        log.info("[before] {} args={}", joinPoint.getSignature(), args);
    }

    @After("execution(* site.timecapsulearchive..*(..)) "
        + "&& !within(site.timecapsulearchive.core.infra..config..*)"
        + "&& !within(site.timecapsulearchive.core.domain..api..*)"
        + "&& !within(site.timecapsulearchive.core.global..*)")
    public void doTraceAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
