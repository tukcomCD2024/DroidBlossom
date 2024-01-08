package site.timecapsulearchive.core.domain.capsule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "내가 생성한 캡슐 페이징")
@Validated
public record MyCapsulePageResponse(
    @JsonProperty("groups")
    @Schema(description = "캡슐 요약 정보 리스트")
    @Valid
    List<CapsuleSummaryResponse> capsules,

    @JsonProperty("hasNext")
    @Schema(description = "다음 페이지 유무")
    Boolean hasNext,

    @JsonProperty("hasPrevious")
    @Schema(description = "이전 페이지 유무")
    Boolean hasPrevious
) {

}
