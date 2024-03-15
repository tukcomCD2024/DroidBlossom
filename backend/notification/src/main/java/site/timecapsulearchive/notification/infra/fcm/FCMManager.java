package site.timecapsulearchive.notification.infra.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.entity.CategoryName;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendableException;
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

    public void send(
        CapsuleSkinNotificationSendDto dto,
        CategoryName categoryName,
        String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .putData(TOPIC_DATA_NAME, String.valueOf(categoryName))
                        .putData(STATUS_DATA_NAME, String.valueOf(dto.status()))
                        .putData(TITLE_DATA_NAME, dto.title())
                        .putData(TEXT_DATA_NAME, dto.text())
                        .putData(IMAGE_DATA_NAME,
                            s3PreSignedUrlManager.createS3PreSignedUrlForGet(dto.skinUrl()))
                        .setToken(fcmToken)
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendableException(e);
        }
    }
}
