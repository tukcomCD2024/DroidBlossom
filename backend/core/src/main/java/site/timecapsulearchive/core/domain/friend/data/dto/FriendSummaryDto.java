package site.timecapsulearchive.core.domain.friend.data.dto;

import java.time.ZonedDateTime;
import site.timecapsulearchive.core.domain.friend.data.response.FriendSummaryResponse;

public record FriendSummaryDto(
    Long id,
    String profileUrl,
    String nickname,
    ZonedDateTime createdAt
) {

    public FriendSummaryResponse toResponse() {
        return FriendSummaryResponse.builder()
            .id(id)
            .profileUrl(profileUrl)
            .nickname(nickname)
            .createdAt(createdAt)
            .build();
    }
}
