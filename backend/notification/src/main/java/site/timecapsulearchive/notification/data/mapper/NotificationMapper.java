package site.timecapsulearchive.notification.data.mapper;

import org.springframework.stereotype.Component;
import site.timecapsulearchive.notification.data.dto.CapsuleSkinNotificationSendDto;
import site.timecapsulearchive.notification.data.request.CapsuleSkinNotificationSendRequest;
import site.timecapsulearchive.notification.entity.Notification;
import site.timecapsulearchive.notification.entity.NotificationCategory;

@Component
public class NotificationMapper {

    public CapsuleSkinNotificationSendDto capsuleSkinNotificationRequestToDto(
        CapsuleSkinNotificationSendRequest request) {
        return CapsuleSkinNotificationSendDto.builder()
            .memberId(request.memberId())
            .skinName(request.skinName())
            .title(request.title())
            .text(request.text())
            .skinUrl(request.skinUrl())
            .build();
    }

    public Notification capsuleSkinNotificationSendDtoToEntity(CapsuleSkinNotificationSendDto dto,
        NotificationCategory notificationCategory) {
        return Notification.builder()
            .memberId(dto.memberId())
            .title(dto.title())
            .text(dto.text())
            .imageUrl(dto.skinUrl())
            .notificationCategory(notificationCategory)
            .build();
    }
}
