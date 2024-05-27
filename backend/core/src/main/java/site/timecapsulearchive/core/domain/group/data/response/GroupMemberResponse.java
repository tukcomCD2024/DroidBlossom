package site.timecapsulearchive.core.domain.group.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "그룹원 정보")
public record GroupMemberResponse(

    @Schema(description = "그룹원 아이디")
    Long memberId,

    @Schema(description = "그룹원 프로필 url")
    String profileUrl,

    @Schema(description = "그룹원 닉네임")
    String nickname,

    @Schema(description = "그룹원 태그")
    String tag,

    @Schema(description = "그룹장 여부")
    Boolean isOwner,

    @Schema(description = "친구 여부")
    Boolean isFriend
) {

}