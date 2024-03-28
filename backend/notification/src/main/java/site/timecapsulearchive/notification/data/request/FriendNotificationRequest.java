package site.timecapsulearchive.notification.data.request;

import site.timecapsulearchive.notification.entity.NotificationStatus;

public record FriendNotificationRequest(
    Long memberId,
    NotificationStatus status,
    String text,
    String title
) {

}
