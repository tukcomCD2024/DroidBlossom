package site.timecapsulearchive.core.domain.friend.data.dto;

import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.friend.data.request.SearchFriendsRequest;
import site.timecapsulearchive.core.domain.friend.data.response.SearchFriendSummaryResponse;
import site.timecapsulearchive.core.domain.friend.exception.FriendNotFoundException;

@Builder
public record SearchFriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    byte[] phone,
    Boolean isFriend,
    Boolean isFriendRequest
) {

    public SearchFriendSummaryResponse toResponse(
        final SearchFriendsRequest request,
        final Function<byte[], String> aesEncryptionFunction
    ) {
        return request.phoneBook().entrySet().stream()
            .filter(phoneBook -> aesEncryptionFunction.apply(phone).equals(phoneBook.getKey()))
            .map(phoneBook -> toResponse(phoneBook.getValue(), aesEncryptionFunction))
            .findFirst()
            .orElseThrow(FriendNotFoundException::new);
    }

    public SearchFriendSummaryResponse toResponse(
        final String originName,
        final Function<byte[], String> aesEncryptionFunction
    ) {
        return SearchFriendSummaryResponse.builder()
            .id(id)
            .profileUrl(profileUrl)
            .originName(originName)
            .nickname(nickname)
            .phone(aesEncryptionFunction.apply(phone))
            .isFriend(isFriend)
            .isFriendRequest(isFriendRequest)
            .build();
    }
}
