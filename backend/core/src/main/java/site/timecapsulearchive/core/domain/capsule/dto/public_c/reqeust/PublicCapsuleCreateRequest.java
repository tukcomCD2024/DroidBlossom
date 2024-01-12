package site.timecapsulearchive.core.domain.capsule.dto.public_c.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "캡슐 생성 포맷")
@Validated
public record PublicCapsuleCreateRequest(

    @Schema(description = "미디어들(이미지, 비디오)")
    @Valid
    List<MultipartFile> media,

    @Schema(description = "캡슐 스킨 아이디")
    Long capsuleSkinId,

    @Schema(description = "제목")
    String title,

    @Schema(description = "내용")
    String content,

    @Schema(description = "현재 경도")
    Float longitude,

    @Schema(description = "현재 위도")
    Float latitude,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate
) {

}
