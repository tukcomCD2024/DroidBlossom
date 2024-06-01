package site.timecapsulearchive.core.domain.member_group.data.dto;

import java.time.ZonedDateTime;
import java.util.function.Function;
import lombok.Builder;
import site.timecapsulearchive.core.domain.member_group.data.response.GroupReceptionInviteSummaryResponse;

@Builder
public record GroupInviteSummaryDto(

    Long groupId,
    String groupName,
    String groupProfileUrl,
    String description,
    ZonedDateTime createdAt,
    String groupOwnerName
) {

    public GroupReceptionInviteSummaryResponse toResponse(
        final Function<String, String> preSignedUrlFunction
    ) {
        return GroupReceptionInviteSummaryResponse.builder()
            .groupId(groupId)
            .groupName(groupName)
            .groupProfileUrl(preSignedUrlFunction.apply(groupProfileUrl))
            .description(description)
            .createdAt(createdAt)
            .groupOwnerName(groupOwnerName)
            .build();
    }

}