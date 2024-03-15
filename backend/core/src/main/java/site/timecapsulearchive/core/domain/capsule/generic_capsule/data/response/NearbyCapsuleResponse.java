package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "현재 위치로 거리 이내 캡슐 정보")
public record NearbyCapsuleResponse(

    @Schema(description = "캡슐 요약 정보 리스트")
    List<CapsuleSummaryResponse> capsules
) {

    public static NearbyCapsuleResponse from(List<CapsuleSummaryResponse> capsules) {
        return new NearbyCapsuleResponse(capsules);
    }
}
