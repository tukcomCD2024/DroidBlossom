package site.timecapsulearchive.core.domain.member_group.data.dto;

public record GroupOwnerSummaryDto(
    String nickname,
    Boolean isOwner,
    String groupProfileUrl
) {

}
