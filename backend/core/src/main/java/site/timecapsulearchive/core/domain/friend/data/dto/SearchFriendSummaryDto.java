package site.timecapsulearchive.core.domain.friend.data.dto;

public record SearchFriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    byte[] phone,
    Boolean isFriend,
    Boolean isFriendRequest
) {

}
