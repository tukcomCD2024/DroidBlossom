package site.timecapsulearchive.core.domain.member_group.data.response;

import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
public record GroupSendingInviteMemberResponse(
    Long groupInviteId,
    Long memberId,
    String nickname,
    String profileUrl,
    ZonedDateTime sendingInvitesCreatedAt
) {
    public GroupSendingInviteMemberResponse {
        if (sendingInvitesCreatedAt != null) {
           sendingInvitesCreatedAt = sendingInvitesCreatedAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}
