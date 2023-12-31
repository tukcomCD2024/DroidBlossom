package site.timecapsulearchive.core.domain.group.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "그룹 요약 정보")
@Validated
public record GroupSummaryResponse(
    @JsonProperty("id")
    @Schema(description = "그룹 아이디")
    Long id,

    @JsonProperty("name")
    @Schema(description = "그룹 이름")
    String name,

    @JsonProperty("profileUrl")
    @Schema(description = "그룹 프로필 url")
    String profileUrl,

    @JsonProperty("description")
    @Schema(description = "그룹 설명")
    String description
) {

}