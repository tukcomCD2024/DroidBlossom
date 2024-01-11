package site.timecapsulearchive.core.domain.history.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "히스토리 요약 정보")
@Validated
public record HistorySummaryResponse(

    @Schema(description = "제목")
    String title,

    @Schema(description = "히스토리 썸네일 url")
    String thumbnailUrl
) {

}