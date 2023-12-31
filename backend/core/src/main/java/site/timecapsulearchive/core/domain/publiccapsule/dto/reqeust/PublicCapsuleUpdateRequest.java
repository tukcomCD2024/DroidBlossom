package site.timecapsulearchive.core.domain.publiccapsule.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "공개 캡슐 업데이트 포맷")
@Validated
public record PublicCapsuleUpdateRequest(
    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("content")
    @Schema(description = "내용")
    String content,

    @JsonProperty("media")
    @Schema(description = "미디어들(이미지, 동영상)")
    @Valid
    List<MultipartFile> media,

    @JsonProperty("dueDate")
    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @JsonProperty("capsuleSkinId")
    @Schema(description = "캡슐 스킨 아이디")
    Long capsuleSkinId
) {

}
