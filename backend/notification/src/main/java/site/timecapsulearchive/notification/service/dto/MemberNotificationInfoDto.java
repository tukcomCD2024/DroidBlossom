package site.timecapsulearchive.notification.service.dto;

public record MemberNotificationInfoDto(
    String fcmToken,
    Boolean notificationEnabled
) {

}