package site.timecapsulearchive.core.domain.group.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹원 상세 정보")
public record GroupMemberDetailResponse(

    @Schema(description = "그룹원 아이디")
    Long id,

    @Schema(description = "그룹원 이름")
    String nickname,

    @Schema(description = "프로필 url")
    String profileUrl,

    @Schema(description = "사용자와 친구 여부")
    Boolean isFriend
) {

}