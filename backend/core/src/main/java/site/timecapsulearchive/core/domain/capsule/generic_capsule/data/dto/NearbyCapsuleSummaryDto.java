package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyCapsuleSummaryResponse;

public record NearbyCapsuleSummaryDto(
    Long id,
    Point point,
    CapsuleType capsuleType
) {

    public NearbyCapsuleSummaryResponse toResponse(Function<Point, Point> geoTransformFunction) {
        Point changedPoint = geoTransformFunction.apply(point);

        return NearbyCapsuleSummaryResponse.builder()
            .id(id)
            .latitude(changedPoint.getX())
            .longitude(changedPoint.getY())
            .capsuleType(capsuleType)
            .build();
    }
}
