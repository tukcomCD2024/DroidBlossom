package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.CapsuleOpenStatus;

@Schema(description = "그룹 캡슐 개봉 상태 응답")
public record GroupCapsuleOpenStateResponse(

    @Schema(description = "캡슐 개봉 상태")
    CapsuleOpenStatus capsuleOpenStatus,

    @Schema(description = "캡슐 개봉 상태 메시지")
    String statusMessage,

    @Schema(description = "현재 요청한 사용자의 개별적인 캡슐 개봉 상태")
    boolean isIndividuallyOpened
) {

}
