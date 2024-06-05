package site.timecapsulearchive.core.domain.member_group.data.dto;

import java.time.ZonedDateTime;

public record GroupSendingInvitesRequestDto(
    Long memberId,
    Long groupId,
    int size,
    ZonedDateTime createdAt
) {

    public static GroupSendingInvitesRequestDto create(
        final Long memberId,
        final Long groupId,
        final int size,
        final ZonedDateTime createdAt
    ) {
        return new GroupSendingInvitesRequestDto(memberId, groupId, size, createdAt);
    }
}
