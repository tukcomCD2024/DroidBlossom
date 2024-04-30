package site.timecapsulearchive.core.global.config.rabbitmq;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RabbitmqComponentConstants {

    CAPSULE_SKIN_QUEUE("request.createCapsuleSkin.queue", "fail.createCapsuleSkin.queue"),
    CAPSULE_SKIN_EXCHANGE("request.createCapsuleSkin.exchange", "fail.createCapsuleSkin.exchange"),
    FRIEND_REQUEST_NOTIFICATION_QUEUE("request.notifyFriendRequest.queue", "fail.notifyFriendRequest.queue"),
    FRIEND_REQUEST_NOTIFICATION_EXCHANGE("request.notifyFriendRequest.exchange", "fail.notifyFriendRequest.exchange"),
    FRIEND_ACCEPT_NOTIFICATION_QUEUE("request.notifyFriendAccept.queue", "fail.notifyFriendAccept.queue"),
    FRIEND_ACCEPT_NOTIFICATION_EXCHANGE("request.notifyFriendAccept.exchange", "fail.notifyFriendAccept.exchange"),
    GROUP_INVITE_QUEUE("request.notifyGroupInvite.queue", "fail.notifyGroupInvite.queue"),
    GROUP_INVITE_EXCHANGE("request.notifyGroupInvite.exchange", "fail.notifyGroupInvite.exchange");

    private final String successComponent;
    private final String failComponent;

    RabbitmqComponentConstants(String successComponent, String failComponent) {
        this.successComponent = successComponent;
        this.failComponent = failComponent;
    }

    public static String getFailComponent(String successComponent) {
        return Arrays.stream(RabbitmqComponentConstants.values())
            .filter(constants -> constants.getSuccessComponent().equals(successComponent))
            .map(RabbitmqComponentConstants::getFailComponent)
            .toList()
            .get(0);
    }
}
