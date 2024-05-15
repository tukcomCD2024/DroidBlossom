package site.timecapsulearchive.core.domain.group_member.data;

public record GroupOwnerSummaryDto(
    String nickname,
    Boolean isOwner,
    String groupProfileUrl
) {

}
