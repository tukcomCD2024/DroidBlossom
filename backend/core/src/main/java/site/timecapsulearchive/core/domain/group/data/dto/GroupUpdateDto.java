package site.timecapsulearchive.core.domain.group.data.dto;

import lombok.Builder;

@Builder
public record GroupUpdateDto(
    String groupName,
    String groupDescription,
    String groupImageProfileFileName,
    String groupImageDirectory
) {

}
