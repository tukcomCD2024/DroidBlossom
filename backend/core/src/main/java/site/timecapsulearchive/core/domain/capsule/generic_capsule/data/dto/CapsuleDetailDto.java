package site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto;

import java.time.ZonedDateTime;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

public record CapsuleDetailDto(
    Long capsuleId,
    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    String nickname,
    String profileUrl,
    ZonedDateTime createdAt,
    Point point,
    String address,
    String roadName,
    String title,
    String content,
    String images,
    String videos,
    Boolean isOpened,
    CapsuleType capsuleType
) {

    public CapsuleDetailDto excludeTitleAndContentAndImagesAndVideos() {
        return new CapsuleDetailDto(
            capsuleId,
            capsuleSkinUrl,
            dueDate,
            nickname,
            profileUrl,
            createdAt,
            point,
            address,
            roadName,
            "",
            "",
            "",
            "",
            isOpened,
            capsuleType
        );
    }
}
