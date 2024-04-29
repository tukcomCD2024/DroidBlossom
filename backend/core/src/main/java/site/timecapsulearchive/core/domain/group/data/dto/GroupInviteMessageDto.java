package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;
import site.timecapsulearchive.core.infra.queue.data.dto.NotificationRequestMessage;

@Builder
public record GroupInviteMessageDto(

    String groupProfileUrl,
    NotificationStatus notificationStatus,
    String title,
    String text,
    List<Long> targetIds
) {

    public static GroupInviteMessageDto createOf(
        final String groupProfileUrl,
        final String ownerNickname,
        final List<Long> targetIds

    ) {
        NotificationRequestMessage notificationRequestMessage = NotificationRequestMessage.GROUP_INVITE;

        return GroupInviteMessageDto.builder()
            .groupProfileUrl(groupProfileUrl)
            .notificationStatus(notificationRequestMessage.getStatus())
            .title(notificationRequestMessage.getTitle())
            .text(notificationRequestMessage.buildPrefixText(ownerNickname))
            .targetIds(targetIds)
            .build();
    }

}