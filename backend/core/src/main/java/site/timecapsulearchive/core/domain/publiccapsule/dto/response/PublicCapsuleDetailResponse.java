package site.timecapsulearchive.core.domain.publiccapsule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "공개 캡슐 상세 정보")
@Validated
public record PublicCapsuleDetailResponse(
    @JsonProperty("capsuleSkinUrl")
    @Schema(description = "캡슐 스킨 url")
    String capsuleSkinUrl,

    @JsonProperty("dueDate")
    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @JsonProperty("nickname")
    @Schema(description = "닉네임")
    String nickname,

    @JsonProperty("createdDate")
    @Schema(description = "생성일")
    ZonedDateTime createdDate,

    @JsonProperty("address")
    @Schema(description = "캡슐 생성 주소")
    String address,

    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("content")
    @Schema(description = "내용")
    String content,

    @JsonProperty("mediaUrls")
    @Schema(description = "미디어 url들")
    @Valid
    List<String> mediaUrls,

    @JsonProperty("isOpened")
    @Schema(description = "개봉 유무")
    Boolean isOpened
) {

}
