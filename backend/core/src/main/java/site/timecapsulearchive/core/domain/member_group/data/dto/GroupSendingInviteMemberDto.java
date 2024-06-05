package site.timecapsulearchive.core.domain.member_group.data.dto;


import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupSendingInviteMemberResponse;

public record GroupSendingInviteMemberDto(
    Long id,
    String nickname,
    String profileUrl,
    ZonedDateTime sendingInvitesCreatedAt
) {

    public GroupSendingInviteMemberResponse toResponse() {
        return GroupSendingInviteMemberResponse.builder()
            .id(id)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .sendingInvitesCreatedAt(sendingInvitesCreatedAt)
            .build();
    }
}
