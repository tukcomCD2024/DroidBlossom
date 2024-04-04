package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "검색된 친구 정보")
public record SearchFriendSummaryResponse(

    @Schema(description = "검색된 사용자 아이디")
    Long id,

    @Schema(description = "검색된 사용자 프로필")
    String profileUrl,

    @Schema(description = "검색된 사용자 닉네임")
    String nickname,

    @Schema(description = "친구 유무")
    Boolean isFriend,

    @Schema(description = "친구 요청 유무")
    Boolean isFriendRequest

) {

}
