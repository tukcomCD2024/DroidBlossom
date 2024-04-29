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
    List<Long> targetIds
) {

    public Group toEntity() {
        return Group.builder()
            .groupName(groupName)
            .groupDescription(description)
            .groupProfileUrl(groupProfileUrl)
            .build();
    }
}