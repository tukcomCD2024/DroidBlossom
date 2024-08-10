package site.timecapsulearchive.notification.global.config.rabbitmq;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RabbitmqComponentConstants {

    CAPSULE_SKIN_NOTIFICATION_QUEUE("notification.createCapsuleSkin.queue",
        "fail.notification.createCapsuleSkin.queue"),
    CAPSULE_SKIN_NOTIFICATION_EXCHANGE("notification.createCapsuleSkin.exchange",
        "fail.notification.createCapsuleSkin.exchange"),
    FRIEND_REQUEST_NOTIFICATION_QUEUE("notification.friendRequest.queue",
        "fail.notification.friendRequest.queue"),
    FRIEND_REQUEST_NOTIFICATION_EXCHANGE("notification.friendRequest.exchange",
        "fail.notification.friendRequest.exchange"),
    BATCH_FRIEND_REQUESTS_NOTIFICATION_QUEUE("batch.notification.friendRequests.queue",
        "fail.batch.notification.friendRequests.queue"),
    BATCH_FRIEND_REQUESTS_NOTIFICATION_EXCHANGE("batch.notification.friendRequests.exchange",
        "fail.batch.notification.friendRequests.exchange"),
    FRIEND_ACCEPT_NOTIFICATION_QUEUE("notification.friendAccept.queue",
        "fail.notification.friendAccept.queue"),
    FRIEND_ACCEPT_NOTIFICATION_EXCHANGE("notification.friendAccept.exchange",
        "fail.notification.friendAccept.exchange"),
    GROUP_INVITE_NOTIFICATION_QUEUE("notification.groupInvite.queue",
        "fail.notification.groupInvite.queue"),
    GROUP_INVITE_NOTIFICATION_EXCHANGE("notification.groupInvite.exchange",
        "fail.notification.groupInvite.exchange"),
    GROUP_ACCEPT_NOTIFICATION_QUEUE("notification.groupAccept.queue",
        "fail.notification.groupAccept.queue"),
    GROUP_ACCEPT_NOTIFICATION_EXCHANGE("notification.groupAccept.exchange",
        "fail.notification.groupAccept.exchange");

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