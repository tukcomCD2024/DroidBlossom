package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import site.timecapsulearchive.core.domain.group.data.response.GroupSummaryResponse;

public record GroupSummaryDto(
    Long id,
    String groupName,
    String groupDescription,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    Boolean isOwner
) {

    public GroupSummaryResponse toResponse(final Function<String, String> preSignedUrlFunction) {
        return GroupSummaryResponse.builder()
            .id(id)
            .name(groupName)
            .description(groupDescription)
            .profileUrl(preSignedUrlFunction.apply(groupProfileUrl))
            .createdAt(createdAt)
            .isOwner(isOwner)
            .build();
    }
}
