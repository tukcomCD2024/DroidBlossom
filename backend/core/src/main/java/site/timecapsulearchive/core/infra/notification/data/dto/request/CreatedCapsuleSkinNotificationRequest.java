package site.timecapsulearchive.core.infra.notification.data.dto.request;

public record CreatedCapsuleSkinNotificationRequest(

    Long memberId,

    String status,

    String skinName,

    String title,

    String text,

    String skinUrl
) {

}
