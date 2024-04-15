package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyARCapsuleSummaryDto;

@Schema(description = "현재 위치로 거리 이내 AR 캡슐 정보")
public record NearbyARCapsuleResponse(

    @Schema(description = "AR 캡슐 요약 정보 리스트")
    List<NearbyARCapsuleSummaryResponse> capsules
) {

    public static NearbyARCapsuleResponse createOf(
        final List<NearbyARCapsuleSummaryDto> dtos,
        final Function<Point, Point> geoTransformFunction,
        final Function<String, String> preSignUrlFunction
    ) {
        final List<NearbyARCapsuleSummaryResponse> capsules = dtos.stream()
            .map(dto -> {
                    Point point = geoTransformFunction.apply(dto.point());
                    return dto.toResponse(point, preSignUrlFunction);
                }
            )
            .toList();

        return new NearbyARCapsuleResponse(capsules);
    }
}
