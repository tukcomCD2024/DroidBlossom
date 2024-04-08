package site.timecapsulearchive.core.domain.friend.data.dto;

import site.timecapsulearchive.core.domain.friend.data.response.SearchTagFriendSummaryResponse;

public record SearchTagFriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    Boolean isFriend,
    Boolean isFriendRequest
) {

    public SearchTagFriendSummaryResponse toResponse() {
        return SearchTagFriendSummaryResponse.builder()
            .id(id)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .isFriend(isFriend)
            .isFriendRequest(isFriendRequest)
            .build();
    }

}
