package site.timecapsulearchive.notification.infra.fcm.friend;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.List;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;
import site.timecapsulearchive.notification.entity.CategoryName;

public interface FriendFcmManager {

    void sendFriendNotification(
        final FriendNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) throws FirebaseMessagingException;

    void sendFriendNotifications(
        final FriendNotificationsDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) throws FirebaseMessagingException;
}
