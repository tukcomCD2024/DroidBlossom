package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import java.util.List;
import site.timecapsulearchive.core.domain.capsule.generic_capsule.data.dto.CapsuleDetailDto;
import site.timecapsulearchive.core.domain.group.data.dto.GroupMemberSummaryDto;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupCapsuleDetailDto(
    CapsuleDetailDto capsuleDetailDto,
    List<GroupMemberSummaryDto> members
) {

    public List<GroupMemberSummaryResponse> toGroupMemberSummaryResponse() {
        return members.stream()
            .map(GroupMemberSummaryDto::toResponse)
            .toList();
    }

    public GroupCapsuleDetailDto excludeDetailContents() {
        return new GroupCapsuleDetailDto(
            capsuleDetailDto.excludeTitleAndContentAndImagesAndVideos(),
            members
        );
    }
}
