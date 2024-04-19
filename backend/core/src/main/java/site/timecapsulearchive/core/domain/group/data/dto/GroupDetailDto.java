package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupDetailDto(
    String groupName,
    String groupDescription,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    List<GroupMemberSummaryDto> members
) {

    public GroupDetailResponse toResponse() {
        List<GroupMemberSummaryResponse> members = this.members.stream()
            .map(GroupMemberSummaryDto::toResponse)
            .toList();

        return GroupDetailResponse.builder()
            .groupName(groupName)
            .groupDescription(groupDescription)
            .groupProfileUrl(groupProfileUrl)
            .createdAt(createdAt)
            .members(members)
            .build();
    }
}
