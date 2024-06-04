package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record GroupDetailDto(
    String groupName,
    String groupDescription,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    Boolean isOwner,
    List<GroupMemberDto> members
) {

    public static GroupDetailDto as(
        String groupName,
        String groupDescription,
        String groupProfileUrl,
        ZonedDateTime createdAt,
        Boolean isOwner,
        List<GroupMemberDto> members
    ) {
        return new GroupDetailDto(groupName, groupDescription, groupProfileUrl, createdAt, isOwner,
            members);
    }

}
