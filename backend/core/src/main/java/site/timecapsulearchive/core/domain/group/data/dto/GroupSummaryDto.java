package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;

public record GroupSummaryDto(
    Long id,
    String groupName,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    Boolean isOwner
) {

}
