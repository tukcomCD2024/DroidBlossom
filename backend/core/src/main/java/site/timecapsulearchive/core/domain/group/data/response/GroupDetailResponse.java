package site.timecapsulearchive.core.domain.group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
@Schema(description = "그룹 생성 포맷")
public record GroupDetailResponse(

    @Schema(description = "그룹 이름")
    String groupName,

    @Schema(description = "그룹 프로필 url")
    String groupProfileUrl,

    @Schema(description = "그룹 설명")
    String groupDescription,

    @Schema(description = "그룹 생성일")
    ZonedDateTime createdAt,

    @Schema(description = "그룹원 리스트")
    List<GroupMemberSummaryResponse> members
) {

    public GroupDetailResponse {
        if (createdAt != null) {
            createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}