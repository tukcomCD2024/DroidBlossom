package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberResponse;

public record GroupCapsuleMemberDto(
    Long id,
    String nickname,
    String profileUrl,
    Boolean isGroupOwner,
    Boolean isOpened
) {

    public GroupCapsuleMemberResponse toResponse() {
        return new GroupCapsuleMemberResponse(id, nickname, profileUrl, isGroupOwner, isOpened);
    }
}
