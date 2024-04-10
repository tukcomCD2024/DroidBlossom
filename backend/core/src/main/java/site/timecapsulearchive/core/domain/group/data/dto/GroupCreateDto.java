package site.timecapsulearchive.core.domain.group.data.dto;

import java.util.List;
import lombok.Builder;
import site.timecapsulearchive.core.domain.group.entity.Group;

@Builder
public record GroupCreateDto(
    String groupName,
    String groupImage,
    String groupProfileUrl,
    String description,
    List<Long> memberIds
) {

    public Group toEntity() {
        return Group.builder()
            .groupName(groupName)
            .groupDescription(description)
            .groupProfileUrl(groupProfileUrl)
            .build();
    }

    public GroupInviteMessageDto toInviteMessageDto(String ownerName) {
        return GroupInviteMessageDto.builder()
            .ownerName(ownerName)
            .groupName(groupName)
            .groupImage(groupImage)
            .groupProfileUrl(groupProfileUrl)
            .description(description)
            .memberIds(memberIds)
            .build();

    }
}