package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.function.Function;
import lombok.Builder;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.NearbyCapsuleSummaryDto;

@Schema(description = "캡슐 요약 정보")
@Builder
public record NearbyCapsuleSummaryResponse(

    @Schema(description = "캡슐 아이디")
    Long id,

    @Schema(description = "캡슐 경도 좌표")
    Double longitude,

    @Schema(description = "캡슐 위도 좌표")
    Double latitude,

    @Schema(description = "캡슐 타입")
    CapsuleType capsuleType
) {

    public static NearbyCapsuleSummaryResponse createOf(
        NearbyCapsuleSummaryDto dto,
        Function<Point, Point> geoTransformFunction
    ) {
        Point point = geoTransformFunction.apply(dto.point());

        return new NearbyCapsuleSummaryResponse(
            dto.id(),
            point.getY(),
            point.getX(),
            dto.capsuleType()
        );
    }
}