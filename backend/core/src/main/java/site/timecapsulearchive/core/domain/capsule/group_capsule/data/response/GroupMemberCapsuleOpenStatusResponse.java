package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "그룹원별 캡슐 개봉 상태")
@Builder
public record GroupMemberCapsuleOpenStatusResponse(

    @Schema(description = "회원 아이디")
    Long memberId,

    @Schema(description = "회원 닉네임")
    String nickname,

    @Schema(description = "회원 프로필")
    String profileUrl,

    @Schema(description = "개봉 상태")
    boolean isOpened
) {

}
