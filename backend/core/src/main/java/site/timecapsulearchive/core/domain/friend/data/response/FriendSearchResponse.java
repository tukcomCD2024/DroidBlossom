package site.timecapsulearchive.core.domain.friend.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FriendSearchResponse(
    @Schema(description = "친구 아이디")
    Long id,

    @Schema(description = "친구 프로필")
    String profileUrl,

    @Schema(description = "친구 닉네임")
    String nickname,

    @Schema(description = "친구 관계")
    Boolean isFriend
) {

}
