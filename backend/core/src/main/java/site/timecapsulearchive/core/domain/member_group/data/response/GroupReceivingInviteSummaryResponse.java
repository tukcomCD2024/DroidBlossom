package site.timecapsulearchive.core.domain.member_group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
@Schema(description = "초대온 그룹 요약 정보")
public record GroupReceivingInviteSummaryResponse(

    @Schema(description = "그룹 아이디")
    Long groupId,

    @Schema(description = "그룹 이름")
    String groupName,

    @Schema(description = "그룹 프로필 url")
    String groupProfileUrl,

    @Schema(description = "그룹 설명")
    String description,

    @Schema(description = "그룹 초대 시간")
    ZonedDateTime groupReceivingInviteTime,

    @Schema(description = "그룹장")
    String groupOwnerName
) {

    public GroupReceivingInviteSummaryResponse {
        if (groupReceivingInviteTime != null) {
            groupReceivingInviteTime = groupReceivingInviteTime.withZoneSameInstant(
                ResponseMappingConstant.ZONE_ID);
        }
    }
}