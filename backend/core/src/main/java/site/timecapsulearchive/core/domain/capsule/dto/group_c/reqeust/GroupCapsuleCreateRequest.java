package site.timecapsulearchive.core.domain.capsule.dto.group_c.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "그룹 캡슐 생성 포맷")
@Validated
public record GroupCapsuleCreateRequest(
    @JsonProperty("media")
    @Schema(description = "미디어들(이미지, 비디오)")
    @Valid
    List<MultipartFile> media,

    @JsonProperty("capsuleSkinId")
    @Schema(description = "캡슐 스킨 아이디")
    Long capsuleSkinId,

    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("content")
    @Schema(description = "내용")
    String content,

    @JsonProperty("longitude")
    @Schema(description = "경도")
    Float longitude,

    @JsonProperty("latitude")
    @Schema(description = "위도")
    Float latitude,

    @JsonProperty("dueDate")
    @Schema(description = "생성일")
    ZonedDateTime dueDate
) {

}