package site.timecapsulearchive.notification.infra.fcm.group;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.List;
import site.timecapsulearchive.notification.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.entity.CategoryName;

public interface GroupFcmManager {

    void sendGroupInviteNotifications(
        final GroupInviteNotificationDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) throws FirebaseMessagingException;

    void sendGroupAcceptNotification(
        final GroupAcceptNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) throws FirebaseMessagingException;
}
