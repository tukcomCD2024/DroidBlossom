package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.UnaryOperator;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleSummaryResponse;

public record GroupCapsuleSummaryDto(
    Long groupId,
    String groupName,
    String groupProfileUrl,
    String creatorNickname,
    String creatorProfileUrl,
    String skinUrl,
    String title,
    ZonedDateTime dueDate,
    Point point,
    String address,
    String roadName,
    Boolean isOpened,
    ZonedDateTime createdAt,
    List<GroupCapsuleMemberDto> groupMembers
) {

    public GroupCapsuleSummaryResponse toResponse(
        final UnaryOperator<String> preSignUrlFunction,
        final UnaryOperator<Point> changePointFunction
    ) {
        final Point changePoint = changePointFunction.apply(point);

        final List<GroupCapsuleMemberResponse> groupMembersResponse = groupMembers.stream()
            .map(GroupCapsuleMemberDto::toResponse)
            .toList();

        return GroupCapsuleSummaryResponse.builder()
            .groupId(groupId)
            .groupMembers(groupMembersResponse)
            .groupName(groupName)
            .groupProfileUrl(preSignUrlFunction.apply(groupProfileUrl))
            .creatorNickname(creatorNickname)
            .creatorProfileUrl(creatorProfileUrl)
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
