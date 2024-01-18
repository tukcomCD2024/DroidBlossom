package site.timecapsulearchive.core.domain.history.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "히스토리 생성 요청")
public record HistoryCreateRequest(

    @Schema(description = "제목")
    String title,

    @Schema(description = "이미지 아이디들")
    List<Long> imageIds
) {

}
