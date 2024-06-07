package site.timecapsulearchive.core.infra.queue.manager;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.core.global.config.rabbitmq.RabbitmqComponentConstants;
import site.timecapsulearchive.core.infra.queue.data.dto.FriendAcceptNotificationDto;
import site.timecapsulearchive.core.infra.queue.data.dto.FriendReqNotificationDto;
import site.timecapsulearchive.core.infra.queue.data.dto.FriendsReqNotificationsDto;
import site.timecapsulearchive.core.infra.queue.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.core.infra.queue.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.core.infra.queue.data.dto.TreasureCaptureNotificationDto;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class SocialNotificationManager {

    private final RabbitTemplate basicRabbitTemplate;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

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
        final String ownerNickname,
        final String profileUrl,
        final List<Long> targetIds
    ) {
        if (targetIds.isEmpty()) {
            return;
        }

        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.FRIEND_REQUEST_NOTIFICATION_QUEUE.getSuccessComponent(),
            FriendsReqNotificationsDto.createOf(ownerNickname, profileUrl, targetIds)
        );
    }

    public void sendGroupInviteMessage(
        final String ownerNickname,
        final String groupProfileUrl,
        final List<Long> targetIds
    ) {
        String preSignedUrl = s3PreSignedUrlManager.getS3PreSignedUrlForGet(groupProfileUrl);

        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.GROUP_INVITE_NOTIFICATION_QUEUE.getSuccessComponent(),
            GroupInviteNotificationDto.createOf(ownerNickname, preSignedUrl, targetIds)
        );
    }

    public void sendGroupAcceptMessage(
        final String groupMemberNickname,
        final Long targetId
    ) {
        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.GROUP_ACCEPT_NOTIFICATION_QUEUE.getSuccessComponent(),
            GroupAcceptNotificationDto.createOf(targetId, groupMemberNickname)
        );
    }
    public void sendTreasureCaptureMessage(
        final Long targetId,
        final String memberNickname,
        final String treasureUrl
    ) {
        String preSignedUrl = s3PreSignedUrlManager.getS3PreSignedUrlForGet(treasureUrl);

        basicRabbitTemplate.convertAndSend(
            RabbitmqComponentConstants.TREASURE_CAPTURE_NOTIFICATION_EXCHANGE.getSuccessComponent(),
            RabbitmqComponentConstants.TREASURE_CAPTURE_NOTIFICATION_QUEUE.getSuccessComponent(),
            TreasureCaptureNotificationDto.createOf(targetId, preSignedUrl, memberNickname)
        );
    }
}
