package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.UnaryOperator;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberResponse;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleSummaryResponse;

public record CombinedGroupCapsuleSummaryDto(
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
    Boolean isCapsuleOpened,
    ZonedDateTime createdAt,
    Boolean isRequestMemberCapsuleOpen,
    Boolean isRequestMemberGroupOwner,
    List<GroupCapsuleMemberDto> groupMembers
) {

    public static CombinedGroupCapsuleSummaryDto create(
        final GroupCapsuleSummaryDto groupCapsuleSummaryDto,
        final List<GroupCapsuleMemberDto> groupCapsuleMemberSummaryDtos,
        final Boolean isRequestMemberCapsuleOpen,
        final Boolean isRequestMemberGroupOwner
    ) {
        return new CombinedGroupCapsuleSummaryDto(
            groupCapsuleSummaryDto.groupId(),
            groupCapsuleSummaryDto.groupName(),
            groupCapsuleSummaryDto.groupProfileUrl(),
            groupCapsuleSummaryDto.creatorNickname(),
            groupCapsuleSummaryDto.creatorProfileUrl(),
            groupCapsuleSummaryDto.skinUrl(),
            groupCapsuleSummaryDto.title(),
            groupCapsuleSummaryDto.dueDate(),
            groupCapsuleSummaryDto.point(),
            groupCapsuleSummaryDto.address(),
            groupCapsuleSummaryDto.roadName(),
            groupCapsuleSummaryDto.isOpened(),
            groupCapsuleSummaryDto.createdAt(),
            isRequestMemberCapsuleOpen,
            isRequestMemberGroupOwner,
            groupCapsuleMemberSummaryDtos
        );
    }

    public GroupCapsuleSummaryResponse toResponse(
        final UnaryOperator<String> preSignUrlFunction,
        final UnaryOperator<Point> changePointFunction
    ) {
        final Point changePoint = changePointFunction.apply(point);

        final List<GroupCapsuleMemberResponse> groupMembers = this.groupMembers.stream()
            .map(GroupCapsuleMemberDto::toResponse)
            .toList();

        return GroupCapsuleSummaryResponse.builder()
            .groupId(groupId)
            .groupMembers(groupMembers)
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
            .isCapsuleOpened(isCapsuleOpened)
            .isRequestMemberCapsuleOpened(isRequestMemberCapsuleOpen)
            .isRequestMemberGroupOwner(isRequestMemberGroupOwner)
            .createdAt(createdAt)
            .build();
    }
}
