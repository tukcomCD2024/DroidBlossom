package site.timecapsulearchive.core.infra.queue.data.dto;

import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.domain.notification.entity.NotificationStatus;

@Builder
public record GroupInviteNotificationDto(

    String groupProfileUrl,
    NotificationStatus notificationStatus,
    String title,
    String text,
    List<Long> targetIds
) {

    public static GroupInviteNotificationDto createOf(
        final String ownerNickname,
        final String groupProfileUrl,
        final List<Long> targetIds

    ) {
        NotificationRequestMessage notificationRequestMessage = NotificationRequestMessage.GROUP_INVITE;

        return GroupInviteNotificationDto.builder()
            .groupProfileUrl(groupProfileUrl)
            .notificationStatus(notificationRequestMessage.getStatus())
            .title(notificationRequestMessage.getTitle())
            .text(notificationRequestMessage.buildPrefixText(ownerNickname))
            .targetIds(targetIds)
            .build();
    }

}