package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹원 요약 정보")
public record GroupCapsuleMemberSummaryResponse(

    @Schema(description = "닉네임")
    String nickname,

    @Schema(description = "프로필 url")
    String profileUrl,

    @Schema(description = "개봉 여부")
    Boolean isOpened
) {

}