package site.timecapsulearchive.core.domain.member_group.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupReceivingInviteSummaryResponse;

@Builder
public record GroupInviteSummaryDto(

    Long groupId,
    String groupName,
    String groupProfileUrl,
    String description,
    ZonedDateTime groupReceivingInviteTime,
    String groupOwnerName
) {

    public GroupReceivingInviteSummaryResponse toResponse(
        final Function<String, String> preSignedUrlFunction
    ) {
        return GroupReceivingInviteSummaryResponse.builder()
            .groupId(groupId)
            .groupName(groupName)
            .groupProfileUrl(preSignedUrlFunction.apply(groupProfileUrl))
            .description(description)
            .groupReceivingInviteTime(groupReceivingInviteTime)
            .groupOwnerName(groupOwnerName)
            .build();
    }

}