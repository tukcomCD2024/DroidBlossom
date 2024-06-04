package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.function.Function;
import site.timecapsulearchive.core.domain.group.data.response.GroupSummaryResponse;

public record CompleteGroupSummaryDto(

    GroupSummaryDto groupSummaryDto,
    String groupOwnerProfileUrl,
    Long totalGroupMemberCount

) {

    public GroupSummaryResponse toResponse(final Function<String, String> preSignedUrlFunction) {
        return GroupSummaryResponse.builder()
            .id(groupSummaryDto.id())
            .name(groupSummaryDto.groupName())
            .groupOwnerProfileUrl(groupOwnerProfileUrl)
            .profileUrl(preSignedUrlFunction.apply(groupSummaryDto.groupProfileUrl()))
            .totalGroupMemberCount(totalGroupMemberCount)
            .createdAt(groupSummaryDto.createdAt())
            .isOwner(groupSummaryDto.isOwner())
            .build();
    }

}
