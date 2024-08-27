package site.timecapsulearchive.core.domain.friend.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;

@Builder
public record SearchFriendSummaryDtoByTag(
    Long id,
    String tag,
    String profileUrl,
    String nickname,
    Boolean isFriend,
    Boolean isFriendInviteToFriend,
    Boolean isFriendInviteToMe
) {

    public SearchTagFriendSummaryResponse toResponse() {
        return SearchTagFriendSummaryResponse.builder()
            .id(id)
            .tag(tag)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .isFriend(isFriend)
            .isFriendInviteToFriend(isFriendInviteToFriend)
            .isFriendInviteToMe(isFriendInviteToMe)
            .build();
    }

}
