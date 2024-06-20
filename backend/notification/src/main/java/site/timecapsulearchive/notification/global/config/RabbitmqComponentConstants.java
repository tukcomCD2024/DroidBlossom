package site.timecapsulearchive.notification.global.config;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RabbitmqComponentConstants {

    CAPSULE_SKIN_QUEUE("notification.createCapsuleSkin.queue",
        "fail.notification.createCapsuleSkin.queue"),
    CAPSULE_SKIN_EXCHANGE("notification.createCapsuleSkin.exchange",
        "fail.notification.createCapsuleSkin.exchange"),
    FRIEND_REQUEST_NOTIFICATION_QUEUE("notification.friendRequest.queue",
        "fail.notification.friendRequest.queue"),
    FRIEND_REQUEST_NOTIFICATION_EXCHANGE("notification.friendRequest.exchange",
        "fail.notification.friendRequest.exchange"),
    FRIEND_REQUESTS_NOTIFICATION_QUEUE("notification.friendRequests.queue",
        "fail.notification.friendRequests.queue"),
    FRIEND_REQUESTS_NOTIFICATION_EXCHANGE("notification.friendRequests.exchange",
        "fail.notification.friendRequests.exchange"),
    FRIEND_ACCEPT_NOTIFICATION_QUEUE("notification.friendAccept.queue",
        "fail.notification.friendAccept.queue"),
    FRIEND_ACCEPT_NOTIFICATION_EXCHANGE("notification.friendAccept.exchange",
        "fail.notification.friendAccept.exchange"),
    GROUP_INVITE_QUEUE("notification.groupInvite.queue", "fail.notification.groupInvite.queue"),
    GROUP_INVITE_EXCHANGE("notification.groupInvite.exchange",
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