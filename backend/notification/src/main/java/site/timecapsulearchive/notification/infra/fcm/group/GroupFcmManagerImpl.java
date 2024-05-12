package site.timecapsulearchive.notification.infra.fcm.group;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import java.util.List;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.infra.fcm.FcmMessageData;

@Component
public class GroupFcmManagerImpl implements GroupFcmManager {

    public void sendGroupInviteNotifications(
        final GroupInviteNotificationDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) throws FirebaseMessagingException {
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
    }

    public void sendGroupAcceptNotification(
        final GroupAcceptNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) throws FirebaseMessagingException {
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

    }
}
