package site.timecapsulearchive.core.infra.notification.manager;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.mapper.NotificationMapper;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;


@Component
@RequiredArgsConstructor
public class NotificationManager {

    private static final String NOTIFICATION_SERVER_URL = "https://notification.archive-timecapsule.kro.kr/api/notification/capsule_skin/send";
    private final RestTemplate restTemplate;
    private final NotificationMapper notificationMapper;

    public void sendCreatedSkinMessage(
        final Long memberId,
        final CapsuleSkinCreateDto dto
    ) {
        final CreatedCapsuleSkinNotificationRequest request = notificationMapper.toRequest(memberId, dto);

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            restTemplate.postForEntity(
                NOTIFICATION_SERVER_URL,
                new HttpEntity<>(request, headers),
                Void.class
            );
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }
}
