package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.NearbyARCapsuleSummaryResponse;

public record NearbyARCapsuleSummaryDto(
    Long id,
    Point point,
    String nickname,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public NearbyARCapsuleSummaryResponse toResponse(
        final Function<Point, Point> geoTransformFunction,
        final Function<String, String> preSignUrlFunction
    ) {
        Point curPoint = geoTransformFunction.apply(point);

        return NearbyARCapsuleSummaryResponse.builder()
            .id(id)
            .latitude(curPoint.getX())
            .longitude(curPoint.getY())
            .nickname(nickname)
            .capsuleSkinUrl(preSignUrlFunction.apply(skinUrl))
            .title(title)
            .dueDate(dueDate)
            .capsuleType(capsuleType)
            .build();
    }
}
