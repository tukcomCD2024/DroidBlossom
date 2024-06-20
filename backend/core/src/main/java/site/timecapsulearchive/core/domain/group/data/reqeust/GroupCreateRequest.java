package site.timecapsulearchive.core.domain.group.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import site.timecapsulearchive.core.domain.group.data.dto.GroupCreateDto;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;

@Schema(description = "그룹 생성 포맷")
public record GroupCreateRequest(

    @Schema(description = "그룹 이름")
    @NotBlank(message = "그룹 이름은 필수 입니다.")
    String groupName,

    @Schema(description = "그룹 이미지")
    @Image
    String groupImage,

    @Schema(description = "그룹 이미지 디렉토리")
    @NotBlank(message = "그룹 이미지 디렉토리는 필수 입니다.")
    String groupDirectory,

    @Schema(description = "그룹 설명")
    @NotBlank(message = "그룹 설명은 필수 입니다.")
    String description,

    @Schema(description = "그룹원 아이디들")
    @NotNull(message = "그룹원 아이디들은 필수 입니다.")
    @Size(max = 30, message = "그룹 초대원은 최대 30명까지 입니다.")
    List<Long> targetIds

) {

    public GroupCreateDto toDto(String url) {
        return GroupCreateDto.builder()
            .groupName(groupName)
            .groupImage(groupImage)
            .description(description)
            .groupProfileUrl(url)
            .targetIds(targetIds)
            .build();
    }
}