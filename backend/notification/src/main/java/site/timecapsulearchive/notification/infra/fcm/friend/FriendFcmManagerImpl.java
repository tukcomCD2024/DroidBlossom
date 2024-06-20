package site.timecapsulearchive.notification.infra.fcm.friend;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendAbleException;
import site.timecapsulearchive.notification.infra.fcm.FCMManager;
import site.timecapsulearchive.notification.infra.fcm.FcmMessageData;

@Component
@RequiredArgsConstructor
public class FriendFcmManagerImpl implements FriendFcmManager {

    private final FCMManager fcmManager;

    public void sendFriendNotification(
        final FriendNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(FcmMessageData.TOPIC.getData(), String.valueOf(categoryName))
                        .putData(FcmMessageData.STATUS.getData(),
                            String.valueOf(dto.notificationStatus()))
                        .putData(FcmMessageData.TITLE.getData(), dto.title())
                        .putData(FcmMessageData.TEXT.getData(), dto.text())
                        .setToken(fcmToken)
                        .build()
                );
        } catch (FirebaseMessagingException exception) {
            throw new MessageNotSendAbleException(exception);
        }
    }

    public void sendFriendNotifications(
        final FriendNotificationsDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) {
        try {
            FirebaseMessaging.getInstance()
                .sendEachForMulticast(
                    MulticastMessage.builder()
                        .addAllTokens(fcmTokens)
                        .putData(FcmMessageData.TOPIC.getData(), String.valueOf(categoryName))
                        .putData(FcmMessageData.STATUS.getData(),
                            String.valueOf(dto.notificationStatus()))
                        .putData(FcmMessageData.TITLE.getData(), dto.title())
                        .putData(FcmMessageData.TEXT.getData(), dto.text())
                        .build()
                );
        } catch (FirebaseMessagingException exception) {
            throw new MessageNotSendAbleException(exception);
        }
    }
}
