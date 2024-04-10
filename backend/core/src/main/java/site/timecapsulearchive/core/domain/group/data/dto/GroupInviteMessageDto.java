package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GroupInviteMessageDto(
    String ownerName,
    String groupName,
    String groupImage,
    String groupProfileUrl,
    String description,
    List<Long> memberIds
) {

}