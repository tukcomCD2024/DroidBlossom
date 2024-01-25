package site.timecapsulearchive.core.domain.capsule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "현재 위치로 거리 이내 캡슐 정보")
public record NearbyCapsulePageResponse(

    @Schema(description = "캡슐 요약 정보 리스트")
    List<CapsuleSummaryResponse> capsules,

    @Schema(description = "다음 캡슐 유무")
    Boolean hasNext,

    @Schema(description = "이전 캡슐 유무")
    Boolean hasPrevious
) {

}
