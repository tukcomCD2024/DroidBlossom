package site.timecapsulearchive.core.domain.capsule.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Schema(description = "현재 위치로 거리 이내 캡슐 정보")
@Validated
public record NearbyCapsulePageResponse(
    @JsonProperty("groups")
    @Schema(description = "캡슐 요약 정보 리스트")
    @Valid
    List<CapsuleSummaryResponse> capsules,

    @JsonProperty("hasNext")
    @Schema(description = "다음 캡슐 유무")
    Boolean hasNext,

    @JsonProperty("hasPrevious")
    @Schema(description = "이전 캡슐 유무")
    Boolean hasPrevious
) {

}
