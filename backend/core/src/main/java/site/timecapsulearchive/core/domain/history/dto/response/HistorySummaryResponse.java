package site.timecapsulearchive.core.domain.history.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "히스토리 요약 정보")
@Validated
public record HistorySummaryResponse(
    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("thumbnailUrl")
    @Schema(description = "히스토리 썸네일 url")
    String thumbnailUrl
) {

}