package site.timecapsulearchive.core.domain.group.data.dto;

import site.timecapsulearchive.core.domain.group.data.response.GroupMemberSummaryResponse;

public record GroupMemberSummaryDto(
    Long memberId,
    String profileUrl,
    String nickname,
    String tag,
    Boolean isOwner
) {

    public GroupMemberSummaryResponse toResponse() {
        return GroupMemberSummaryResponse.builder()
            .memberId(memberId)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .build();
    }
}
