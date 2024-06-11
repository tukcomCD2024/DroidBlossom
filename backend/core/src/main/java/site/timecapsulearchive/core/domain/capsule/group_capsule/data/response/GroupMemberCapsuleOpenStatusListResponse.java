package site.timecapsulearchive.core.domain.capsule.group_capsule.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto.GroupMemberCapsuleOpenStatusDto;

@Schema(description = "그룹원들 캡슐 개봉 상태")
public record GroupMemberCapsuleOpenStatusListResponse(
    List<GroupMemberCapsuleOpenStatusResponse> groupMemberCapsuleOpenStatus
) {

    public static GroupMemberCapsuleOpenStatusListResponse create(
        List<GroupMemberCapsuleOpenStatusDto> groupMemberCapsuleOpenStatus) {
        List<GroupMemberCapsuleOpenStatusResponse> groupMemberCapsuleOpenStatusResponses = groupMemberCapsuleOpenStatus.stream()
            .map(GroupMemberCapsuleOpenStatusDto::toResponse)
            .toList();

        return new GroupMemberCapsuleOpenStatusListResponse(groupMemberCapsuleOpenStatusResponses);
    }
}
