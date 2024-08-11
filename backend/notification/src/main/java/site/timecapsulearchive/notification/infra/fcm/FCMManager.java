package site.timecapsulearchive.notification.infra.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMManager {

    private final FCMProperties fcmProperties;

    @PostConstruct
    public void initialize() throws IOException {
        log.info("fcm manager 초기화 시작");
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(getCredential()))
            .setProjectId(fcmProperties.projectId())
            .build();

        FirebaseApp.initializeApp(options);
        log.info("fcm manager 초기화 완료");
    }

    private InputStream getCredential() throws IOException {
        return new ClassPathResource(fcmProperties.secretKeyPath()).getInputStream();
    }
}
