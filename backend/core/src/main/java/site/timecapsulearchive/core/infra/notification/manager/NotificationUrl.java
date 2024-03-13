package site.timecapsulearchive.core.infra.notification.manager;

public enum NotificationUrl {
    CAPSULE_SKIN_ALARM_URL(
        "https://notification.archive-timecapsule.kro.kr/api/notification/capsule_skin/send"),
    FRIEND_REQ_ALARM_URL(
        "https://notification.archive-timecapsule.kro.kr/api/notification/friend_req/send");

    private final String url;

    NotificationUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }
}
