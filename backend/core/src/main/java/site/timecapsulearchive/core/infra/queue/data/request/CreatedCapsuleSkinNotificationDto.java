package site.timecapsulearchive.core.infra.queue.data.request;

import lombok.Builder;
import site.timecapsulearchive.core.domain.capsuleskin.data.dto.CapsuleSkinCreateDto;
import site.timecapsulearchive.core.domain.member.entity.NotificationStatus;
import site.timecapsulearchive.core.infra.queue.data.dto.NotificationRequestMessage;

@Builder
public record CreatedCapsuleSkinNotificationDto(
    Long memberId,
    NotificationStatus notificationStatus,
    String skinName,
    String title,
    String text,
    String skinUrl
) {

    public static CreatedCapsuleSkinNotificationDto createOf(final Long memberId, final CapsuleSkinCreateDto dto) {
        final NotificationRequestMessage capsuleSkinRequest = NotificationRequestMessage.CAPSULE_SKIN;

        return new CreatedCapsuleSkinNotificationDto(
            memberId,
            capsuleSkinRequest.getStatus(),
            dto.skinName(),
            capsuleSkinRequest.getTitle(),
            capsuleSkinRequest.buildPrefixText(dto.skinName()),
            dto.imageUrl()
        );
    }
}
