package site.timecapsulearchive.core.domain.group.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.group.data.response.GroupMemberWithRelationResponse;

@Builder
public record GroupMemberWithRelationDto(
    Long memberId,
    String profileUrl,
    String nickname,
    String tag,
    Boolean isOwner,
    Boolean isFriend,
    Boolean isFriendInviteToFriend,
    Boolean isFriendInviteToMe
) {

    public GroupMemberWithRelationResponse toResponse() {
        return GroupMemberWithRelationResponse.builder()
            .memberId(memberId)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .tag(tag)
            .isOwner(isOwner)
            .isFriend(isFriend)
            .build();
    }
}
