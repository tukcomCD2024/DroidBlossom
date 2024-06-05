package site.timecapsulearchive.core.domain.member_group.data.dto;


import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupSendingInviteMemberResponse;

public record GroupSendingInviteMemberDto(
    Long groupInviteId,
    Long memberId,
    String nickname,
    String profileUrl,
    ZonedDateTime sendingInvitesCreatedAt
) {

    public GroupSendingInviteMemberResponse toResponse() {
        return GroupSendingInviteMemberResponse.builder()
            .groupInviteId(groupInviteId)
            .memberId(memberId)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .sendingInvitesCreatedAt(sendingInvitesCreatedAt)
            .build();
    }
}
