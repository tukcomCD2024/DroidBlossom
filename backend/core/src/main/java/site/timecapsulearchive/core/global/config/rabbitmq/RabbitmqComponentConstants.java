package site.timecapsulearchive.core.global.config.rabbitmq;

import lombok.Getter;

@Getter
public enum RabbitmqComponentConstants {
    CAPSULE_SKIN_QUEUE("capsuleSkin.queue"),
    CAPSULE_SKIN_EXCHANGE("capsuleSkin.exchange"),
    CAPSULE_SKIN_ROUTING_KEY(""),
    CAPSULE_SKIN_DELAY_EXCHANGE("capsuleSkin_delay.exchange"),
    FRIEND_REQUEST_NOTIFICATION_QUEUE("friendRequest.queue"),
    FRIEND_REQUEST_NOTIFICATION_EXCHANGE("friendRequest.exchange"),
    GROUP_INVITE_QUEUE_NAME("groupInvite.queue"),
    GROUP_INVITE_EXCHANGE_NAME("groupInvite.exchange");

    private final String value;

    RabbitmqComponentConstants(String value) {
        this.value = value;
    }
}
