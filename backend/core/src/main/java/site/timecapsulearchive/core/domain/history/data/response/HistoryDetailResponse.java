package site.timecapsulearchive.core.domain.history.data.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "히스토리 상세 정보")
public record HistoryDetailResponse(

    @Schema(description = "제목")
    String title,

    @Schema(description = "이미지 url들")
    List<String> imageUrls
) {

}