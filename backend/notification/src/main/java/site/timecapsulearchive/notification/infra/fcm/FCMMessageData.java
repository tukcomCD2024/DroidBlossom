package site.timecapsulearchive.notification.infra.fcm;

public enum FCMMessageData {
    TOPIC("topic"),
    STATUS("status"),

    TEXT("text"),

    TITLE("title"),
    IMAGE("imageUrl");

    private final String data;

    FCMMessageData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
