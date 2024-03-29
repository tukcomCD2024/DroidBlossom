package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "그룹 캡슐 페이징")
public record GroupCapsulePageResponse(

    @Schema(description = "그룹 캡슐 요약 정보 리스트")
    List<GroupCapsuleSummaryResponse> groups,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}