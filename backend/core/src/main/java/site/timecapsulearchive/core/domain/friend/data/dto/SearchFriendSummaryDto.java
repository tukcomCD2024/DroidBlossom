package site.timecapsulearchive.core.domain.friend.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendSummaryResponse;

@Builder
public record SearchFriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    byte[] phone,
    byte[] phoneHash,
    Boolean isFriend,
    Boolean isFriendRequest
) {

    public SearchFriendSummaryResponse toResponse(
        final PhoneBook phoneBook
    ) {
        return SearchFriendSummaryResponse.builder()
            .id(id)
            .profileUrl(profileUrl)
            .originName(phoneBook.originName())
            .nickname(nickname)
            .phone(phoneBook.phone())
            .isFriend(isFriend)
            .isFriendRequest(isFriendRequest)
            .build();
    }
}
