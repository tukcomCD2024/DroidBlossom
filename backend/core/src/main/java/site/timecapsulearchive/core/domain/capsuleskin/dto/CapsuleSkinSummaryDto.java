package site.timecapsulearchive.core.domain.capsuleskin.dto;

import java.time.ZonedDateTime;

public record CapsuleSkinSummaryDto(
    Long id,
    String skinUrl,
    String name,
    ZonedDateTime createdAt
) {

}
