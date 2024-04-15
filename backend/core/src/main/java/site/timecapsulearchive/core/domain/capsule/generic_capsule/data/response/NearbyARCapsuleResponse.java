package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;

@Schema(description = "현재 위치로 거리 이내 AR 캡슐 정보")
public record NearbyARCapsuleResponse(

    @Schema(description = "AR 캡슐 요약 정보 리스트")
    List<NearbyARCapsuleSummaryResponse> capsules
) {

    public static NearbyARCapsuleResponse createOf(
        List<NearbyARCapsuleSummaryDto> dtos,
        GeoTransformManager geoTransformManager,
        S3PreSignedUrlManager s3PreSignedUrlManager
    ) {
        List<NearbyARCapsuleSummaryResponse> capsules = dtos.stream()
            .map(dto -> {
                    Point point = geoTransformManager.changePoint3857To4326(dto.point());
                    return dto.toResponse(point,
                        s3PreSignedUrlManager::getS3PreSignedUrlForGet);
                }
            )
            .toList();

        return new NearbyARCapsuleResponse(capsules);
    }
}
