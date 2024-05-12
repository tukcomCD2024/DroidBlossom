package site.timecapsulearchive.notification.infra.fcm.aspect;

import com.google.firebase.FirebaseException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendAbleException;

@Aspect
@Component
public class FireBaseExceptionAspect {

    @AfterThrowing(
        pointcut = "execution(* site.timecapsulearchive.notification.infra.fcm.*.*.*(..))",
        throwing = "e"
    )
    public void FireBaseHandleException(FirebaseException e) {
        throw new MessageNotSendAbleException(e);
    }
}
