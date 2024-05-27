package site.timecapsulearchive.core.domain.group.data.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record GroupDetailDto(
    String groupName,
    String groupDescription,
    String groupProfileUrl,
    ZonedDateTime createdAt,
    List<GroupMemberDto> members
) {

}
