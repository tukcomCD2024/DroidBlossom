package site.timecapsulearchive.core.domain.capsuleskin.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "캡슐 스킨 요약 정보")
@Validated
public record CapsuleSkinSummaryResponse(
    @Schema(description = "캡슐 스킨 아이디")
    @JsonProperty("id")
    Long id,

    @Schema(description = "캡슐 스킨 url")
    @JsonProperty("skinUrl")
    String skinUrl,

    @Schema(description = "캡슐 스킨 이름")
    @JsonProperty("name")
    String name
) {

}
