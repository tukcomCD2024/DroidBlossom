package site.timecapsulearchive.core.domain.friend.data.dto;

import java.time.ZonedDateTime;

public record FriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    ZonedDateTime createdAt
) {

}
