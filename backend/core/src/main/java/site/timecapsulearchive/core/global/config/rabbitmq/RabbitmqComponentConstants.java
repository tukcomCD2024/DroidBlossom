package site.timecapsulearchive.core.global.config.rabbitmq;

import lombok.Getter;

@Getter
public enum RabbitmqComponentConstants {
    CAPSULE_SKIN_QUEUE("capsuleSkin.queue"),
    CAPSULE_SKIN_EXCHANGE("capsuleSkin.exchange"),
    CAPSULE_SKIN_ROUTING_KEY(""),
    FRIEND_REQUEST_NOTIFICATION_QUEUE("friendRequest.queue"),
    FRIEND_REQUEST_NOTIFICATION_EXCHANGE("friendRequest.exchange"),
    FRIEND_REQUEST_NOTIFICATION_ROUTING_KEY("friendRequest.queue"),
    ;

    private final String value;

    RabbitmqComponentConstants(String value) {
        this.value = value;
    }
}
