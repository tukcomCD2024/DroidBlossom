package site.timecapsulearchive.core.domain.friend.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendSummaryResponse;
import site.timecapsulearchive.core.global.common.wrapper.ByteArrayWrapper;

@Builder
public record SearchFriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    ByteArrayWrapper phoneHash,
    Boolean isFriend,
    Boolean friendInviteToFriend,
    Boolean friendInviteToMe
) {

    public SearchFriendSummaryResponse toResponse(
        final PhoneBook phoneBook
    ) {
        return SearchFriendSummaryResponse.builder()
            .id(id)
            .profileUrl(profileUrl)
            .originName(phoneBook.originName())
            .nickname(nickname)
            .phone(phoneBook.originPhone())
            .isFriend(isFriend)
            .isFriendInviteToFriend(friendInviteToFriend)
            .isFriendInviteToMe(friendInviteToMe)
            .build();
    }
}
