package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.friend.data.dto.FriendSummaryDto;

@Schema(description = "친구 리스트")
public record FriendsSliceResponse(
    @Schema(description = "친구 요약 정보 리스트")
    List<FriendSummaryResponse> friends,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext
) {

    public static FriendsSliceResponse createOf(List<FriendSummaryDto> content, boolean hasNext) {
        List<FriendSummaryResponse> friends = content.stream()
            .map(FriendSummaryDto::toResponse)
            .toList();

        return new FriendsSliceResponse(friends, hasNext);
    }
}