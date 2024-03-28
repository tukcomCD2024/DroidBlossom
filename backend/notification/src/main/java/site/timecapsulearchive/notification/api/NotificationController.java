package site.timecapsulearchive.notification.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.timecapsulearchive.notification.data.mapper.NotificationMapper;
import site.timecapsulearchive.notification.data.request.CapsuleSkinNotificationSendRequest;
import site.timecapsulearchive.notification.data.request.FriendNotificationRequest;
import site.timecapsulearchive.notification.service.NotificationService;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @PostMapping("/capsule_skin/send")
    public ResponseEntity<Void> sendCapsuleSkinNotification(
        @Valid @RequestBody CapsuleSkinNotificationSendRequest request
    ) {
        notificationService.sendCapsuleSkinAlarm(
            notificationMapper.capsuleSkinNotificationRequestToDto(request));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/friend-request/send")
    public ResponseEntity<Void> sendFriendRequestNotification(
        @Valid @RequestBody FriendNotificationRequest request
    ) {
        notificationService.sendFriendRequestsNotification(
            notificationMapper.friendNotificationRequestToDto(request));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/friend-accept/send")
    public ResponseEntity<Void> sendFriendAcceptNotification(
        @Valid @RequestBody FriendNotificationRequest request
    ) {
        notificationService.sendFriendAcceptNotification(
            notificationMapper.friendNotificationRequestToDto(request));

        return ResponseEntity.noContent().build();
    }

}
