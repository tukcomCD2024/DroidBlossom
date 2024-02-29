package site.timecapsulearchive.notification.infra.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.infra.exception.MessageNotSendableException;
import site.timecapsulearchive.notification.infra.s3.S3PreSignedUrlManager;

@Component
@RequiredArgsConstructor
public class FCMManager {

    private static final String CAPSULE_SKIN_TOPIC_NAME = "capsuleSkin";

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
        String title,
        String text,
        String skinUrl,
        String fcmToken
    ) {
        try {
            FirebaseMessaging.getInstance()
                .send(
                    Message.builder()
                        .setTopic(CAPSULE_SKIN_TOPIC_NAME)
                        .setNotification(
                            Notification.builder()
                                .setTitle(title)
                                .setBody(text)
                                .setImage(s3PreSignedUrlManager.createS3PreSignedUrlForGet(skinUrl))
                                .build()
                        )
                        .setToken(fcmToken)
                        .build()
                );
        } catch (FirebaseMessagingException e) {
            throw new MessageNotSendableException("메시지를 보낼 수 없습니다.");
        }
    }
}
