package site.timecapsulearchive.core.domain.group.data.dto;

import site.timecapsulearchive.core.domain.group.data.response.GroupMemberResponse;

public record GroupMemberDto(
    Long memberId,
    String profileUrl,
    String nickname,
    String tag,
    Boolean isOwner
) {

    public GroupMemberResponse toResponse() {
        return GroupMemberResponse.builder()
            .memberId(memberId)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .build();
    }
}
