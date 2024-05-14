package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.util.List;
import java.util.function.Function;
import org.locationtech.jts.geom.Point;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleSummaryResponse;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupCapsuleSummaryDto(
    CapsuleSummaryDto capsuleSummaryDto,
    List<GroupMemberSummaryDto> members
) {

    public List<GroupMemberSummaryResponse> groupMemberSummaryDtoToResponse() {
        return members.stream()
            .map(GroupMemberSummaryDto::toResponse)
            .toList();
    }

    public GroupCapsuleSummaryResponse toResponse(
        final Function<String, String> preSignUrlFunction,
        final Function<Point, Point> changePointFunction
    ) {
        final Point changePoint = changePointFunction.apply(capsuleSummaryDto.point());

        return GroupCapsuleSummaryResponse.builder()
            .members(groupMemberSummaryDtoToResponse())
            .nickname(capsuleSummaryDto.nickname())
            .profileUrl(capsuleSummaryDto.profileUrl())
            .skinUrl(preSignUrlFunction.apply(capsuleSummaryDto.skinUrl()))
            .title(capsuleSummaryDto.title())
            .dueDate(capsuleSummaryDto.dueDate())
            .latitude(changePoint.getX())
            .longitude(changePoint.getY())
            .address(capsuleSummaryDto.address())
            .roadName(capsuleSummaryDto.roadName())
            .isOpened(capsuleSummaryDto.isOpened())
            .createdAt(capsuleSummaryDto.createdAt())
            .build();
    }
}
