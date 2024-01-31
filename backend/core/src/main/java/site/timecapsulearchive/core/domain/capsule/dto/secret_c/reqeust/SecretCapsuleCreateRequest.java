package site.timecapsulearchive.core.domain.capsule.dto.secret_c.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.List;
import site.timecapsulearchive.core.domain.capsule.dto.secret_c.FileMetaData;
import site.timecapsulearchive.core.domain.capsule.entity.CapsuleType;

@Schema(description = "캡슐 생성 포맷")
public record SecretCapsuleCreateRequest(

    @Schema(description = "이미지, 비디오 파일 이름")
    List<FileMetaData> fileNames,

    @Schema(description = "디렉토리 이름")
    String directory,

    @Schema(description = "캡슐 스킨 아이디")
    Long capsuleSkinId,

    @Schema(description = "제목")
    @NotBlank(message = "캡슐 제목은 필수 입니다.")
    String title,

    @Schema(description = "내용")
    @NotBlank(message = "캡슐 내용은 필수 입니다.")
    String content,

    @Schema(description = "현재 경도")
    @NotBlank(message = "캡슐 경도는 필수 입니다.")
    double longitude,

    @Schema(description = "현재 위도")
    @NotBlank(message = "캡슐 위도는 필수 입니다.")
    double latitude,

    @Schema(description = "캡슐 생성 주소")
    @NotBlank(message = "캡슐 생성 주소는 필수 입니다.")
    String address,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 타입")
    @NotBlank(message = "캡슐 타입은 필수 입니다.")
    CapsuleType capsuleType
) {

}
