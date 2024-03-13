package site.timecapsulearchive.core.global.aop.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationRequestExceptionAspect {

    private final RestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();


    @Pointcut("@annotation(site.timecapsulearchive.core.global.aop.anotation.NotificationRequest)")
    public void notificationRequest() {
    }

    @Before("notificationRequest()")
    public void setHttpHeaders() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterThrowing(pointcut = "notificationRequest()", throwing = "ex")
    public void handleExternalApiException(Exception ex) {
        throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR);
    }

    public <T> void sendNotification(T request, String url) {
        try {
            restTemplate.postForEntity(url, new HttpEntity<>(request, headers), Void.class);
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }


}
