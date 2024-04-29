package site.timecapsulearchive.core.infra.queue.manager;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.domain.group.data.dto.GroupInviteMessageDto;
import site.timecapsulearchive.core.global.config.rabbitmq.RabbitmqComponentConstants;
import site.timecapsulearchive.core.infra.queue.data.dto.FriendsReqNotificationDto;
import site.timecapsulearchive.core.infra.queue.data.request.FriendAcceptNotificationDto;
import site.timecapsulearchive.core.infra.queue.data.request.FriendReqNotificationDto;

@Component
@RequiredArgsConstructor
public class SocialNotificationManager {

    private final RabbitTemplate basicRabbitTemplate;

    /**
     * 단건의 친구 요청을 받아서 알림 전송을 요청한다
     * <p>
     * 예를 들면, 친구 검색을 한 후 친구 추가 요청을 보낼 때 사용된다.
     *
     * @param ownerNickname 친구 요청 보내는 사람 닉네임
     * @param friendId      친구 요청 대상 아이디
     */
    public void sendFriendReqMessage(final String ownerNickname, final Long friendId) {
        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getSuccessComponent(),
            FriendReqNotificationDto.createOf(friendId, ownerNickname)
        );
    }

    public void sendFriendAcceptMessage(final String ownerNickname, final Long friendId) {
        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.FRIEND_ACCEPT_NOTIFICATION_QUEUE.getSuccessComponent(),
            FriendAcceptNotificationDto.createOf(friendId, ownerNickname)
        );
    }

    /**
     * 다중의 친구 요청을 받아서 알림 전송을 요청한다.
     * <p>
     * 예를 들면, 전화번호부에서 친구 요청 목록을 받아서 친구 추가 요청을 보낼 때 사용된다.
     *
     * @param ownerNickname 친구 요청 보내는 사람 닉네임
     * @param profileUrl    친구 요청 보내는 사람 프로필
     * @param targetIds     친구 요청 대상 아이디들
     */
    public void sendFriendRequestMessages(
        String ownerNickname,
        String profileUrl,
        List<Long> targetIds
    ) {
        if (targetIds.isEmpty()) {
            return;
        }

        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getSuccessComponent(),
            FriendsReqNotificationDto.createOf(ownerNickname, profileUrl, targetIds)
        );
    }

    public void sendGroupInviteMessage(
        final GroupInviteMessageDto groupInviteMessageDto
    ) {
        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.GROUP_INVITE_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.GROUP_INVITE_QUEUE.getSuccessComponent(),
            groupInviteMessageDto
        );
    }
}
