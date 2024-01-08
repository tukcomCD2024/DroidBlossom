package site.timecapsulearchive.core.domain.capsule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "캡슐 이미지 페이징")
@Validated
public record ImagesPageResponse(
    @JsonProperty("groups")
    @Schema(description = "이미지 요약 정보 리스트")
    @Valid
    List<ImageSummaryResponse> images,

    @JsonProperty("hasNext")
    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @JsonProperty("hasPrevious")
    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
