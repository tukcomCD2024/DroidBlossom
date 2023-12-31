package site.timecapsulearchive.core.domain.capsuleskin.dto.reqeust;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "캡슐 스킨 업데이트 포맷")
@Validated
public record CapsuleSkinUpdateRequest(
    @Schema(description = "캡슐 스킨 이름")
    @JsonProperty("name")
    String name
) {

}