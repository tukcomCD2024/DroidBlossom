package site.timecapsulearchive.core.infra.notification.manager;

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
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendReqNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.mapper.NotificationMapper;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;


@Component
@RequiredArgsConstructor
public class NotificationManager {

    private final RestTemplate restTemplate;
    private final NotificationMapper notificationMapper;

    public void sendCreatedSkinMessage(final Long memberId, final CapsuleSkinCreateDto dto) {
        final CreatedCapsuleSkinNotificationRequest request =
            notificationMapper.capsuleSkinDtoToMessage(memberId, dto);

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            restTemplate.postForEntity(
                NotificationUrl.CAPSULE_SKIN_ALARM_URL.getUrl(),
                new HttpEntity<>(request, headers),
                Void.class
            );
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    public void sendFriendReqMessage(final Long friendId, final String ownerNickname) {
        final FriendReqNotificationRequest request = notificationMapper.friendReqToMessage(friendId,
            ownerNickname);

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            restTemplate.postForEntity(
                NotificationUrl.FRIEND_REQ_ALARM_URL.getUrl(),
                new HttpEntity<>(request, headers),
                Void.class
            );
        } catch (HttpClientErrorException e) {
            throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }
}
