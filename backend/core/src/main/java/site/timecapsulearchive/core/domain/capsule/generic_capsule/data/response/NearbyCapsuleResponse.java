package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;

@Schema(description = "현재 위치로 거리 이내 캡슐 정보")
public record NearbyCapsuleResponse(

    @Schema(description = "캡슐 요약 정보 리스트")
    List<NearbyCapsuleSummaryResponse> capsules
) {

    public static NearbyCapsuleResponse createOf(
        final List<NearbyCapsuleSummaryDto> dtos,
        final Function<Point, Point> geoTransformFunction
    ) {
        final List<NearbyCapsuleSummaryResponse> capsules = dtos.stream()
            .map(dto -> NearbyCapsuleSummaryResponse.createOf(dto, geoTransformFunction))
            .toList();

        return new NearbyCapsuleResponse(capsules);
    }
}
