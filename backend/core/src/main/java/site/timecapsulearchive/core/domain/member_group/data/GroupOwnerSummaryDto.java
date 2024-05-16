package site.timecapsulearchive.core.domain.member_group.data;

public record GroupOwnerSummaryDto(
    String nickname,
    Boolean isOwner,
    String groupProfileUrl
) {

}
