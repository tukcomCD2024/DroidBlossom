package site.timecapsulearchive.notification.global.config;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RabbitmqComponentConstants {

    CAPSULE_SKIN_QUEUE("capsuleSkin.queue", "fail.capsuleSkin.queue"),
    CAPSULE_SKIN_EXCHANGE("capsuleSkin.exchange", "fail.capsuleSkin.exchange"),
    FRIEND_REQUEST_NOTIFICATION_QUEUE("friendRequest.queue", "fail.friendRequest.queue"),
    FRIEND_REQUEST_NOTIFICATION_EXCHANGE("friendRequest.exchange", "fail.friendRequest.exchange"),
    FRIEND_ACCEPT_NOTIFICATION_QUEUE("friendAccept.queue", "fail.friendAccept.queue"),
    FRIEND_ACCEPT_NOTIFICATION_EXCHANGE("friendAccept.exchange", "fail.friendAccept.exchange"),
    GROUP_INVITE_QUEUE("groupInvite.queue", "fail.groupInvite.queue"),
    GROUP_INVITE_EXCHANGE("groupInvite.exchange", "fail.groupInvite.exchange");

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