package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleResponse;

public record GroupCapsuleDto(
    Long capsuleId,
    String capsuleSkinUrl,
    ZonedDateTime dueDate,
    Long groupId,
    String groupName,
    String groupProfileUrl,
    String creatorNickname,
    String creatorProfileUrl,
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

    public GroupCapsuleResponse toResponse(
        UnaryOperator<String> singlePreSignUrlFunction,
        Function<String, List<String>> multiplePreSignUrlFunction,
        UnaryOperator<Point> pointTransformFunction
    ) {
        final Point changePoint = pointTransformFunction.apply(point);

        final List<String> preSignedImageUrls = multiplePreSignUrlFunction.apply(images);
        final List<String> preSignedVideoUrls = multiplePreSignUrlFunction.apply(videos);

        return GroupCapsuleResponse.builder()
            .capsuleId(capsuleId)
            .capsuleSkinUrl(singlePreSignUrlFunction.apply(capsuleSkinUrl))
            .dueDate(dueDate)
            .groupId(groupId)
            .groupName(groupName)
            .groupProfileUrl(singlePreSignUrlFunction.apply(groupProfileUrl))
            .creatorNickname(creatorNickname)
            .creatorProfileUrl(creatorProfileUrl)
            .createdAt(createdAt)
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
            .build();
    }
}
