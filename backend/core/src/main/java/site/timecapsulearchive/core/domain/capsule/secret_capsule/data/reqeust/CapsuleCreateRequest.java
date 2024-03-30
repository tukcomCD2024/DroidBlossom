package site.timecapsulearchive.core.domain.capsule.secret_capsule.data.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import site.timecapsulearchive.core.global.common.valid.annotation.Image;
import site.timecapsulearchive.core.global.common.valid.annotation.Video;
import site.timecapsulearchive.core.infra.map.data.dto.AddressData;

@Schema(description = "캡슐 생성 포맷")
public record CapsuleCreateRequest(

    @Schema(description = "이미지 이름들")
    List<@Image String> imageNames,

    @Schema(description = "비디오들")
    List<@Video String> videoNames,

    @Schema(description = "디렉토리 이름")
    @NotBlank(message = "디렉토리는 필수입니다.")
    String directory,

    @Schema(description = "캡슐 스킨 아이디")
    @NotNull(message = "캡슐 스킨 아이디는 필수입니다.")
    Long capsuleSkinId,

    @Schema(description = "제목")
    @NotBlank(message = "캡슐 제목은 필수 입니다.")
    String title,

    @Schema(description = "내용")
    @NotBlank(message = "캡슐 내용은 필수 입니다.")
    String content,

    @Schema(description = "현재 경도(wsg84)", requiredMode = RequiredMode.REQUIRED)
    double longitude,

    @Schema(description = "현재 위도(wsg84)", requiredMode = RequiredMode.REQUIRED)
    double latitude,

    @Schema(description = "캡슐 생성 주소")
    @NotNull(message = "캡슐 생성 주소는 필수 입니다.")
    AddressData addressData,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate
) {

}
