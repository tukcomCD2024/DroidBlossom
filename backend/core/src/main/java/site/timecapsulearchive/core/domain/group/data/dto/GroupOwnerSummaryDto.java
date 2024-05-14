package site.timecapsulearchive.core.domain.group.data.dto;

public record GroupOwnerSummaryDto(
    String nickname,
    Boolean isOwner,
    String groupProfileUrl
) {

}
