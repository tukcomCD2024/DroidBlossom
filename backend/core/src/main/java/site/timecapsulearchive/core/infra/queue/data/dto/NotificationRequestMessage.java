package site.timecapsulearchive.core.infra.queue.data.dto;

import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;

public enum NotificationRequestMessage {
    CAPSULE_SKIN(NotificationStatus.SUCCESS, "캡슐 스킨 생성 알림", "이 생성되었습니다. ARchive에서 확인해보세요!"),
    FRIEND_REQUEST(NotificationStatus.SUCCESS, "친구 요청 알림", "로부터 친구 요청이 왔습니다. ARchive에서 확인해보세요!"),
    FRIEND_ACCEPT(NotificationStatus.SUCCESS, "친구 수락 알림", "님이 친구 요청을 수락하였습니다. ARchive에서 확인해보세요!");


    NotificationRequestMessage(NotificationStatus status, String title, String text) {
        this.status = status;
        this.title = title;
        this.text = text;
    }

    private final NotificationStatus status;
    private final String title;
    private final String text;

    public NotificationStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String buildPrefixText(String prefix) {
        return prefix + text;
    }
}
