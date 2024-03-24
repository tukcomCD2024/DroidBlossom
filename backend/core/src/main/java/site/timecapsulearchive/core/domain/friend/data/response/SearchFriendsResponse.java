package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.friend.data.dto.SearchFriendSummaryDto;

@Schema(description = "전화번호 목록으로 찾은 회원 요약 정보 리스트 응답")
public record SearchFriendsResponse(

    @Schema(description = "회원 요약 정보")
    List<SearchFriendSummaryResponse> friends
) {

}
