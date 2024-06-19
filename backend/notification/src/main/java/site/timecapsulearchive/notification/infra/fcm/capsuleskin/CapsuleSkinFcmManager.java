package site.timecapsulearchive.notification.infra.fcm.capsuleskin;

import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.entity.CategoryName;

public interface CapsuleSkinFcmManager {

    void sendCapsuleSkinNotification(
        final CapsuleSkinNotificationSendDto dto,
        final CategoryName categoryName,
        final String fcmToken
    );
}
