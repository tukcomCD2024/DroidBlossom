package site.timecapsulearchive.core.domain.capsule.public_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.public_capsule.data.response.PublicCapsuleDetailResponse;

public record PublicCapsuleDetailDto(
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
    CapsuleType capsuleType,
    Boolean isOwner
) {

    public PublicCapsuleDetailDto excludeTitleAndContentAndImagesAndVideos() {
        return new PublicCapsuleDetailDto(
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
            capsuleType,
            isOwner
        );
    }

    public PublicCapsuleDetailResponse toResponse(
        final Function<Point, Point> changePointFunction,
        final Function<String, String> singlePreSignUrlFunction,
        final Function<String, List<String>> multiplePreSignUrlFunction
    ) {
        final Point changePoint = changePointFunction.apply(point);
        final List<String> preSignedImageUrls = multiplePreSignUrlFunction.apply(images);
        final List<String> preSignedVideoUrls = multiplePreSignUrlFunction.apply(videos);

        return PublicCapsuleDetailResponse.builder()
            .capsuleId(capsuleId)
            .capsuleSkinUrl(singlePreSignUrlFunction.apply(capsuleSkinUrl))
            .dueDate(dueDate)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .createdDate(createdAt)
            .latitude(changePoint.getX())
            .longitude(changePoint.getY())
            .address(address)
            .roadName(roadName)
            .title(title)
            .content(content)
            .imageUrls(preSignedImageUrls)
            .videoUrls(preSignedVideoUrls)
            .isOpened(isOpened)
            .capsuleType(capsuleType)
            .isOwner(isOwner)
            .build();
    }
}
