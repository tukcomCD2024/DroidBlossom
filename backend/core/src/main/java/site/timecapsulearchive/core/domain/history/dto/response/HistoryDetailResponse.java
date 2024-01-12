package site.timecapsulearchive.core.domain.history.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "히스토리 상세 정보")
@Validated
public record HistoryDetailResponse(

    @Schema(description = "제목")
    String title,

    @Schema(description = "이미지 url들")
    @Valid
    List<String> imageUrls
) {

}