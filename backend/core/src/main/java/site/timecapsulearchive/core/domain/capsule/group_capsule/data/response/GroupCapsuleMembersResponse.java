package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupCapsuleMemberDto;

@Schema(description = "그룹 캡슐의 그룹원들 목록 응답")
public record GroupCapsuleMembersResponse(
    @Schema(description = "그룹 캡슐의 그룹원들 목록")
    List<GroupCapsuleMemberResponse> groupCapsuleMembers
) {

    public static GroupCapsuleMembersResponse create(
        final List<GroupCapsuleMemberDto> groupCapsuleMembers
    ) {
        List<GroupCapsuleMemberResponse> groupCapsuleMemberResponses = groupCapsuleMembers.stream()
            .map(GroupCapsuleMemberDto::toResponse)
            .toList();

        return new GroupCapsuleMembersResponse(groupCapsuleMemberResponses);
    }
}
