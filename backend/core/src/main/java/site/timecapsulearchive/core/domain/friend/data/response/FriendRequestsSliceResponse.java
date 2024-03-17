package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "친구 요청 리스트")
public record FriendRequestsSliceResponse(
    @Schema(description = "친구 요약 정보 리스트")
    List<FriendSummaryResponse> friends,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

}