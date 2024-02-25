package site.timecapsulearchive.notification.data.request;

public record CapsuleSkinNotificationSendRequest(

    Long memberId,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

}
