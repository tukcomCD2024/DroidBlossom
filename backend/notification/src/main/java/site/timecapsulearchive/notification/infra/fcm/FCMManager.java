package site.timecapsulearchive.notification.infra.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationsDto;
import site.timecapsulearchive.notification.data.dto.GroupAcceptNotificationDto;
import site.timecapsulearchive.notification.data.dto.GroupInviteNotificationDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendAbleException;
import site.timecapsulearchive.notification.infra.s3.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class FCMManager {

    private static final String TOPIC_DATA_NAME = "topic";
    private static final String STATUS_DATA_NAME = "status";
    private static final String TEXT_DATA_NAME = "text";
    private static final String TITLE_DATA_NAME = "title";
    private static final String IMAGE_DATA_NAME = "imageUrl";

    private final FCMProperties fcmProperties;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    @PostConstruct
    public void initialize() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(getCredential()))
            .setProjectId(fcmProperties.projectId())
            .build();

        FirebaseApp.initializeApp(options);
    }

    private InputStream getCredential() throws IOException {
        return new ClassPathResource(fcmProperties.secretKeyPath()).getInputStream();
    }

    public void sendCapsuleSkinNotification(
        final CapsuleSkinNotificationSendDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(TOPIC_DATA_NAME, String.valueOf(categoryName))
                        .putData(STATUS_DATA_NAME, String.valueOf(dto.status()))
                        .putData(TITLE_DATA_NAME, dto.title())
                        .putData(TEXT_DATA_NAME, dto.text())
                        .setToken(fcmToken)
                        .putData(IMAGE_DATA_NAME,
                            s3PreSignedUrlManager.createS3PreSignedUrlForGet(dto.skinUrl()))
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }

    public void sendFriendNotification(
        final FriendNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(TOPIC_DATA_NAME, String.valueOf(categoryName))
                        .putData(STATUS_DATA_NAME, String.valueOf(dto.notificationStatus()))
                        .putData(TITLE_DATA_NAME, dto.title())
                        .putData(TEXT_DATA_NAME, dto.text())
                        .setToken(fcmToken)
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
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
                        .putData(TOPIC_DATA_NAME, String.valueOf(categoryName))
                        .putData(STATUS_DATA_NAME, String.valueOf(dto.notificationStatus()))
                        .putData(TITLE_DATA_NAME, dto.title())
                        .putData(TEXT_DATA_NAME, dto.text())
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }

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
                        .putData(TOPIC_DATA_NAME, String.valueOf(categoryName))
                        .putData(STATUS_DATA_NAME, String.valueOf(dto.notificationStatus()))
                        .putData(TITLE_DATA_NAME, dto.title())
                        .putData(TEXT_DATA_NAME, dto.text())
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }

    public void sendGroupAcceptNotification(
        final GroupAcceptNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(TOPIC_DATA_NAME, String.valueOf(categoryName))
                        .putData(STATUS_DATA_NAME, String.valueOf(dto.notificationStatus()))
                        .putData(TITLE_DATA_NAME, dto.title())
                        .putData(TEXT_DATA_NAME, dto.text())
                        .setToken(fcmToken)
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendAbleException(e);
        }
    }
}
