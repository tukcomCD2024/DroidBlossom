package site.timecapsulearchive.core.domain.capsule.group_capsule.data.dto;

import site.timecapsulearchive.core.domain.capsule.group_capsule.data.response.GroupMemberCapsuleOpenStatusResponse;

public record GroupMemberCapsuleOpenStatusDto(
    Long memberId,
    String nickname,
    String profileUrl,
    boolean isOpened
) {

    public GroupMemberCapsuleOpenStatusResponse toResponse() {
        return GroupMemberCapsuleOpenStatusResponse.builder()
            .memberId(memberId)
            .nickname(nickname)
            .profileUrl(profileUrl)
            .isOpened(isOpened)
            .build();
    }
}
