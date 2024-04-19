package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleSummaryDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupCapsuleSummaryDto(
    CapsuleSummaryDto capsuleSummaryDto,
    List<GroupMemberSummaryDto> members
) {

    public List<GroupMemberSummaryResponse> toGroupMemberSummaryResponse() {
        return members.stream()
            .map(GroupMemberSummaryDto::toResponse)
            .toList();
    }
}
