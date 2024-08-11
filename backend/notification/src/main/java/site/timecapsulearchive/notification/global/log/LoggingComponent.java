package site.timecapsulearchive.notification.global.log;

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

    @Before("@annotation(site.timecapsulearchive.notification.global.log.Trace)")
    public void doTraceBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        log.info("[before] {} args={}", joinPoint.getSignature(), args);
    }

    @After("@annotation(site.timecapsulearchive.notification.global.log.Trace)")
    public void doTraceAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
