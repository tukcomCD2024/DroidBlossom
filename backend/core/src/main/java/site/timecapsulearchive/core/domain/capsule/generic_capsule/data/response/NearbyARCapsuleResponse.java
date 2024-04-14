package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "현재 위치로 거리 이내 AR 캡슐 정보")
public record NearbyARCapsuleResponse(

    @Schema(description = "AR 캡슐 요약 정보 리스트")
    List<NearbyARCapsuleSummaryResponse> capsules
) {

    public static NearbyARCapsuleResponse from(List<NearbyARCapsuleSummaryResponse> capsules) {
        return new NearbyARCapsuleResponse(capsules);
    }
}
