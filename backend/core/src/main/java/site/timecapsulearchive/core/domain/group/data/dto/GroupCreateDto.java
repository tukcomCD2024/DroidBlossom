package site.timecapsulearchive.core.domain.group.data.dto;

import lombok.Builder;
import site.timecapsulearchive.core.domain.group.entity.Group;

@Builder
public record GroupCreateDto(
    String name,
    String groupImage,
    String groupDirectory,
    String description
) {
    public Group toEntity() {
        return Group.builder()
            .groupName(name())
            .groupDescription(description())
            .groupProfileUrl(groupDirectory())
            .build();
    }
}