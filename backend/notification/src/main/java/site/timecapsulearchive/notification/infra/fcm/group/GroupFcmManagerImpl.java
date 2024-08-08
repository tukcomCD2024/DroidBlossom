package site.timecapsulearchive.notification.infra.fcm.group;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.global.aop.Trace;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendAbleException;
import site.timecapsulearchive.notification.infra.fcm.FCMMessageData;
import site.timecapsulearchive.notification.infra.s3.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class GroupFcmManagerImpl implements GroupFcmManager {

    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @Trace
    public void sendGroupInviteNotifications(
        final GroupInviteNotificationDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) {
        try {
            FirebaseMessaging.getInstance()
                .sendEachForMulticast(
                    MulticastMessage.builder()
                        .addAllTokens(fcmTokens)
                        .putData(FCMMessageData.TOPIC.getData(), String.valueOf(categoryName))
                        .putData(FCMMessageData.STATUS.getData(),
                            String.valueOf(dto.notificationStatus()))
                        .putData(FCMMessageData.TITLE.getData(), dto.title())
                        .putData(FCMMessageData.TEXT.getData(), dto.text())
                        .putData(FCMMessageData.IMAGE.getData(),
                            s3PreSignedUrlManager.createS3PreSignedUrlForGet(dto.groupProfileUrl()))
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }

    @Trace
    public void sendGroupAcceptNotification(
        final GroupAcceptNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(FCMMessageData.TOPIC.getData(), String.valueOf(categoryName))
                        .putData(FCMMessageData.STATUS.getData(),
                            String.valueOf(dto.notificationStatus()))
                        .putData(FCMMessageData.TITLE.getData(), dto.title())
                        .putData(FCMMessageData.TEXT.getData(), dto.text())
                        .setToken(fcmToken)
                        .build()
                );

        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }
}
