package site.timecapsulearchive.core.domain.friend.data.dto;

import lombok.Builder;

@Builder
public record SearchFriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    byte[] phone,
    Boolean isFriend,
    Boolean isFriendRequest
) {

}
