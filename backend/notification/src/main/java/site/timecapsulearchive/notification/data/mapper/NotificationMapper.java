package site.timecapsulearchive.notification.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.data.dto.FriendNotificationDto;
import site.timecapsulearchive.notification.data.request.CapsuleSkinNotificationSendRequest;
import site.timecapsulearchive.notification.data.request.FriendNotificationRequest;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;

@Component
public class NotificationMapper {

    public CapsuleSkinNotificationSendDto capsuleSkinNotificationRequestToDto(
        final CapsuleSkinNotificationSendRequest request) {
        return CapsuleSkinNotificationSendDto.builder()
            .memberId(request.memberId())
            .status(request.status())
            .skinName(request.skinName())
            .title(request.title())
            .text(request.text())
            .skinUrl(request.skinUrl())
            .build();
    }

    public Notification capsuleSkinNotificationSendDtoToEntity(
        final CapsuleSkinNotificationSendDto dto,
        final NotificationCategory category) {
        return Notification.builder()
            .memberId(dto.memberId())
            .title(dto.title())
            .text(dto.text())
            .imageUrl(dto.skinUrl())
            .notificationCategory(category)
            .status(dto.status())
            .build();
    }

    public FriendNotificationDto friendNotificationRequestToDto(
        final FriendNotificationRequest request) {
        return FriendNotificationDto.builder()
            .memberId(request.memberId())
            .status(request.status())
            .title(request.title())
            .text(request.text())
            .build();
    }

    public Notification friendNotificationDtoToEntity(final FriendNotificationDto dto,
        final NotificationCategory category) {
        return Notification.builder()
            .memberId(dto.memberId())
            .title(dto.title())
            .text(dto.text())
            .status(dto.status())
            .notificationCategory(category)
            .build();
    }
}
