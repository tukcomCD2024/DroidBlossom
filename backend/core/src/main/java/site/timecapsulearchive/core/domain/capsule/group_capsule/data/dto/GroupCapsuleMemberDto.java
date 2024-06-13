package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberResponse;

public record GroupCapsuleMemberDto(
    String nickname,
    String profileUrl,
    Boolean isRequestMember,
    Boolean isGroupOwner,
    Boolean isOpened
) {

    public GroupCapsuleMemberResponse toResponse() {
        return new GroupCapsuleMemberResponse(nickname, profileUrl, isOpened, isGroupOwner,
            isRequestMember);
    }
}
