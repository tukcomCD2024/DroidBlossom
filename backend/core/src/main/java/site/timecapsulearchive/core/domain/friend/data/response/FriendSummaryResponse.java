package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;
import site.timecapsulearchive.core.global.common.response.ResponseMappingConstant;

@Builder
@Schema(description = "친구 요약 정보")
public record FriendSummaryResponse(
    @Schema(description = "친구 아이디")
    Long id,

    @Schema(description = "친구 프로필")
    String profileUrl,

    @Schema(description = "친구 닉네임")
    String nickname,

    @Schema(description = "친구 수락일")
    ZonedDateTime createdAt
) {

    public FriendSummaryResponse {
        if (createdAt != null) {
            createdAt = createdAt.withZoneSameInstant(ResponseMappingConstant.ZONE_ID);
        }
    }
}
