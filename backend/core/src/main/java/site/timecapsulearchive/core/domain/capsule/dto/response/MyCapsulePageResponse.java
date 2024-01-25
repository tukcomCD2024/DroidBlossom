package site.timecapsulearchive.core.domain.capsule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "내가 생성한 캡슐 페이징")
public record MyCapsulePageResponse(

    @Schema(description = "캡슐 요약 정보 리스트")
    List<CapsuleSummaryResponse> capsules,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
