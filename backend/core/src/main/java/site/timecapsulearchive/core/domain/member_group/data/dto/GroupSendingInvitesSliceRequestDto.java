package site.timecapsulearchive.core.domain.member_group.data.dto;

public record GroupSendingInvitesSliceRequestDto(
    Long memberId,
    Long groupId,
    Long groupInviteId,
    int size
) {

    public static GroupSendingInvitesSliceRequestDto create(
        final Long memberId,
        final Long groupId,
        final Long groupInviteId,
        final int size
    ) {
        return new GroupSendingInvitesSliceRequestDto(memberId, groupId, groupInviteId, size);
    }
}
