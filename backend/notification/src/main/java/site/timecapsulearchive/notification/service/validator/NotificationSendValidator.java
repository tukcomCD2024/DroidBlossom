package site.timecapsulearchive.notification.service.validator;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.service.dto.MemberNotificationInfoDto;

@Component
public class NotificationSendValidator {

    public boolean canSend(MemberNotificationInfoDto memberNotificationInfoDto) {
        return memberNotificationInfoDto != null
            && memberNotificationInfoDto.notificationEnabled()
            && memberNotificationInfoDto.fcmToken() != null
            && !memberNotificationInfoDto.fcmToken().isBlank();
    }
}
