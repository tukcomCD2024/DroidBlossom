package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleSummaryResponse;

public record CapsuleSummaryDto(
    String nickname,
    String profileUrl,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Point point,
    String address,
    String roadName,
    Boolean isOpened,
    ZonedDateTime createdAt
) {

    public CapsuleSummaryResponse toResponse(
        final Function<String, String> preSignUrlFunction,
        final Function<Point, Point> changePointFunction
    ) {
        final Point changePoint = changePointFunction.apply(point);

        return CapsuleSummaryResponse.builder()
            .nickname(nickname)
            .profileUrl(profileUrl)
            .skinUrl(preSignUrlFunction.apply(skinUrl))
            .title(title)
            .dueDate(dueDate)
            .latitude(changePoint.getX())
            .longitude(changePoint.getY())
            .address(address)
            .roadName(roadName)
            .isOpened(isOpened)
            .createdAt(createdAt)
            .build();
    }

}