package site.timecapsulearchive.notification.infra.fcm.capsuleskin;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendAbleException;
import site.timecapsulearchive.notification.infra.fcm.FcmMessageData;
import site.timecapsulearchive.notification.infra.s3.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class CapsuleSkinFcmManagerImpl implements CapsuleSkinFcmManager {

    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    public void sendCapsuleSkinNotification(
        final CapsuleSkinNotificationSendDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(FcmMessageData.TOPIC.getData(), String.valueOf(categoryName))
                        .putData(FcmMessageData.STATUS.getData(), String.valueOf(dto.status()))
                        .putData(FcmMessageData.TITLE.getData(), dto.title())
                        .putData(FcmMessageData.TEXT.getData(), dto.text())
                        .setToken(fcmToken)
                        .putData(FcmMessageData.IMAGE.getData(),
                            s3PreSignedUrlManager.createS3PreSignedUrlForGet(dto.skinUrl()))
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }
}
