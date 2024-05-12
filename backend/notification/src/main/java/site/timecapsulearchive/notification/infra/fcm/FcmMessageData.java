package site.timecapsulearchive.notification.infra.fcm;

public enum FcmMessageData {
    TOPIC("topic"),
    STATUS("status"),

    TEXT("text"),

    TITLE("title"),
    IMAGE("imageUrl");

    FcmMessageData(String data) {
        this.data = data;
    }

    private final String data;

    public String getData() {
        return data;
    }
}
