package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.group.data.response.GroupDetailResponse;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberResponse;

public record GroupDetailDto(
    String groupName,
    String groupDescription,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    List<GroupMemberDto> members
) {

    public GroupDetailResponse toResponse(Function<String, String> singlePreSignUrlFunction) {
        List<GroupMemberResponse> members = this.members.stream()
            .map(GroupMemberDto::toResponse)
            .toList();

        return GroupDetailResponse.builder()
            .groupName(groupName)
            .groupDescription(groupDescription)
            .groupProfileUrl(singlePreSignUrlFunction.apply(groupProfileUrl))
            .createdAt(createdAt)
            .members(members)
            .build();
    }
}
