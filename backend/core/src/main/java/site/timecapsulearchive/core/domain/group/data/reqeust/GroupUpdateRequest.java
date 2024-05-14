package site.timecapsulearchive.core.domain.group.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import site.timecapsulearchive.core.domain.group.data.dto.GroupUpdateDto;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;

@Schema(description = "그룹 업데이트 포맷")
public record GroupUpdateRequest(

    @Schema(description = "그룹 이름")
    @NotBlank(message = "그룹 이름은 필수 입니다.")
    String groupName,

    @Schema(description = "그룹 설명")
    @NotBlank(message = "그룹 설명은 필수 입니다.")
    String groupDescription,

    @Schema(description = "그룹 프로필 이미지 파일 이름")
    @Image
    String groupImageProfileFileName,

    @Schema(description = "그룹 이미지 디렉토리")
    @NotBlank(message = "그룹 이미지 디렉토리는 필수 입니다.")
    String groupImageDirectory
) {

    public GroupUpdateDto toDto() {
        return GroupUpdateDto.builder()
            .groupName(groupName)
            .groupDescription(groupDescription)
            .groupImageProfileFileName(groupImageProfileFileName)
            .groupImageDirectory(groupImageDirectory)
            .build();
    }
}
