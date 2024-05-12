package site.timecapsulearchive.notification.infra.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
import site.timecapsulearchive.notification.infra.fcm.capsuleskin.CapsuleSkinFcmManager;
import site.timecapsulearchive.notification.infra.fcm.friend.FriendFcmManager;
import site.timecapsulearchive.notification.infra.fcm.group.GroupFcmManager;

@Component
@RequiredArgsConstructor
public class FCMManager {

    private final FCMProperties fcmProperties;
    private final CapsuleSkinFcmManager capsuleSkinFcmManager;
    private final FriendFcmManager friendFcmManager;
    private final GroupFcmManager groupFcmManager;

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
        capsuleSkinFcmManager.sendCapsuleSkinNotification(dto, categoryName, fcmToken);
    }

    public void sendFriendNotification(
        final FriendNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        friendFcmManager.sendFriendNotification(dto, categoryName, fcmToken);
    }

    public void sendFriendNotifications(
        final FriendNotificationsDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) {
        friendFcmManager.sendFriendNotifications(dto, categoryName, fcmTokens);
    }

    public void sendGroupInviteNotifications(
        final GroupInviteNotificationDto dto,
        final CategoryName categoryName,
        final List<String> fcmTokens
    ) {
        groupFcmManager.sendGroupInviteNotifications(dto, categoryName, fcmTokens);
    }

    public void sendGroupAcceptNotification(
        final GroupAcceptNotificationDto dto,
        final CategoryName categoryName,
        final String fcmToken
    ) {
        groupFcmManager.sendGroupAcceptNotification(dto, categoryName, fcmToken);
    }
}
