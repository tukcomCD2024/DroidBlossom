package site.timecapsulearchive.core.domain.group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
@Schema(description = "그룹 요약 정보")
public record GroupSummaryResponse(

    @Schema(description = "그룹 아이디")
    Long id,

    @Schema(description = "그룹 이름")
    String name,

    @Schema(description = "그룹 프로필 url")
    String profileUrl,

    @Schema(description = "그룹장 프로필 url")
    String groupOwnerProfileUrl,

    @Schema(description = "총 그룹원 수")
    Long totalGroupMemberCount,

    @Schema(description = "그룹 생성일")
    ZonedDateTime createdAt,

    @Schema(description = "그룹장 여부")
    Boolean isOwner
) {

    public GroupSummaryResponse {
        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}