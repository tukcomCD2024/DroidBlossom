package site.timecapsulearchive.core.infra.notification.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.global.aop.anotation.NotificationRequest;
import site.timecapsulearchive.core.global.aop.aspect.NotificationRequestExceptionAspect;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendReqNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.mapper.NotificationMapper;


@Component
@RequiredArgsConstructor
public class NotificationManager {

    private final NotificationMapper notificationMapper;
    private final NotificationRequestExceptionAspect aspect;

    @NotificationRequest
    public void sendCreatedSkinMessage(final Long memberId, final CapsuleSkinCreateDto dto) {
        final CreatedCapsuleSkinNotificationRequest request =
            notificationMapper.capsuleSkinDtoToMessage(memberId, dto);

        aspect.sendNotification(request, NotificationUrl.CAPSULE_SKIN_ALARM_URL.getUrl());
    }

    @NotificationRequest
    public void sendFriendReqMessage(final Long friendId, final String ownerNickname) {
        final FriendReqNotificationRequest request = notificationMapper.friendReqToMessage(friendId,
            ownerNickname);

        aspect.sendNotification(request, NotificationUrl.FRIEND_REQ_ALARM_URL.getUrl());
    }
}
