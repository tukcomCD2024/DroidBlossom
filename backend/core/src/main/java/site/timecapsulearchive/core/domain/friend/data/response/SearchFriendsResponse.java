package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전화번호 리스트로 찾은 친구")
public record SearchFriendsResponse(

    @Schema(description = "회원 요약 정보")
    List<FriendSummaryResponse> friends
) {

}
