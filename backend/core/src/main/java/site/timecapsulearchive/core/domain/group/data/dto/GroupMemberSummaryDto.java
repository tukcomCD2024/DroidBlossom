package site.timecapsulearchive.core.domain.group.data.dto;

import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupMemberSummaryDto(
    String nickname,
    String profileUrl,
    Boolean isOpened
) {
    public GroupMemberSummaryResponse toResponse() {
        return new GroupMemberSummaryResponse(nickname, profileUrl, isOpened);
    }

}