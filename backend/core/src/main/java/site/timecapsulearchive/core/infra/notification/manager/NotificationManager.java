package site.timecapsulearchive.core.infra.notification.manager;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.global.aop.anotation.NotificationRequest;
import site.timecapsulearchive.core.global.aop.aspect.NotificationRequestExceptionAspect;
import site.timecapsulearchive.core.global.config.rabbitmq.RabbitmqComponentConstants;
import site.timecapsulearchive.core.infra.notification.data.dto.FriendRequestNotificationDto;
import site.timecapsulearchive.core.infra.notification.data.dto.request.CreatedCapsuleSkinNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendAcceptNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.dto.request.FriendReqNotificationRequest;
import site.timecapsulearchive.core.infra.notification.data.mapper.NotificationMapper;


@Component
@RequiredArgsConstructor
public class NotificationManager {

    private final NotificationMapper notificationMapper;
    private final NotificationRequestExceptionAspect aspect;
    private final NotificationUrl notificationUrl;
    private final RabbitTemplate rabbitTemplate;

    @NotificationRequest
    public void sendCreatedSkinMessage(final Long memberId, final CapsuleSkinCreateDto dto) {
        final CreatedCapsuleSkinNotificationRequest request =
            notificationMapper.capsuleSkinDtoToMessage(memberId, dto);

        aspect.sendNotification(request, notificationUrl.capsuleSkinAlarmUrl());
    }

    @NotificationRequest
    public void sendFriendReqMessage(final Long friendId, final String ownerNickname) {
        final FriendReqNotificationRequest request = notificationMapper.friendReqToMessage(friendId,
            ownerNickname);

        aspect.sendNotification(request, notificationUrl.friendReqAlarmUrl());
    }

    @NotificationRequest
    public void sendFriendAcceptMessage(final Long friendId, final String ownerNickname) {
        final FriendAcceptNotificationRequest request = notificationMapper.friendAcceptToMessage(
            friendId,
            ownerNickname);

        aspect.sendNotification(request, notificationUrl.friendAcceptAlarmUrl());
    }

    public void sendFriendRequestMessages(
        String ownerNickname,
        String profileUrl,
        List<Long> targetIds
    ) {
        if (targetIds.isEmpty()) {
            return;
        }

        rabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getValue(),
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_ROUTING_KEY.getValue(),
            FriendRequestNotificationDto.createOf(ownerNickname, profileUrl, targetIds)
        );
    }
}
