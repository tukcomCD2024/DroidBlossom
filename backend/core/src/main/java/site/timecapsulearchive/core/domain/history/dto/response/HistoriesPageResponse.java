package site.timecapsulearchive.core.domain.history.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "히스토리 페이징")
public record HistoriesPageResponse(

    @Schema(description = "히스토리 요약 정보 리스트")
    List<HistorySummaryResponse> histories,

    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
