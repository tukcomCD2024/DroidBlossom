package site.timecapsulearchive.core.domain.history.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "히스토리 상세 정보")
@Validated
public record HistoryDetailResponse(
    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("imageUrls")
    @Schema(description = "이미지 url들")
    @Valid
    List<String> imageUrls
) {

}