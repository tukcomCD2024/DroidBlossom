package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupCapsuleMemberSummaryResponse;

public record GroupCapsuleMemberSummaryDto(
    String nickname,
    String profileUrl,
    Boolean isOpened
) {

    public GroupCapsuleMemberSummaryResponse toResponse() {
        return new GroupCapsuleMemberSummaryResponse(nickname, profileUrl, isOpened);
    }
}