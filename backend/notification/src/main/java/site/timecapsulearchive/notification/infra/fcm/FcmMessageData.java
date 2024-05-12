package site.timecapsulearchive.notification.infra.fcm;

public enum FcmMessageData {
    TOPIC("topic"),
    STATUS("status"),

    TEXT("text"),

    TITLE("title"),
    IMAGE("imageUrl");

    private final String data;

    FcmMessageData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
