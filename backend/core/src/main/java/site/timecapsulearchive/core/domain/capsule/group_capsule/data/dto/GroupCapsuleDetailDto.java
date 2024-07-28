package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.response.CapsuleDetailResponse;

public record GroupCapsuleDetailDto(
    Long groupId,
    Long capsuleId,
    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    Long creatorId,
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
    Boolean isCapsuleOpened,
    CapsuleType capsuleType
) {

    public GroupCapsuleDetailDto excludeTitleAndContentAndImagesAndVideos() {
        return new GroupCapsuleDetailDto(
            groupId,
            capsuleId,
            capsuleSkinUrl,
            dueDate,
            creatorId,
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
            isCapsuleOpened,
            capsuleType
        );
    }

    public CapsuleDetailResponse toResponse(
        final Function<String, String> singlePreSignUrlFunction,
        final Function<String, List<String>> multiplePreSignUrlFunction,
        final Function<Point, Point> changePointFunction
    ) {
        final List<String> preSignedImageUrls = multiplePreSignUrlFunction.apply(
            images);
        final List<String> preSignedVideoUrls = multiplePreSignUrlFunction.apply(
            videos);

        final Point changePoint = changePointFunction.apply(point);

        return CapsuleDetailResponse.builder()
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
            .isOpened(isCapsuleOpened)
            .capsuleType(capsuleType)
            .build();
    }
}
