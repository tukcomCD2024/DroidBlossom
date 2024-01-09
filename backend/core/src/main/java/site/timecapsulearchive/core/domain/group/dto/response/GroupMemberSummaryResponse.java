package site.timecapsulearchive.core.domain.group.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

@Schema(description = "그룹원 요약 정보")
@Validated
public record GroupMemberSummaryResponse(
    @JsonProperty("nickname")
    @Schema(description = "닉네임")
    String nickname,

    @JsonProperty("profileUrl")
    @Schema(description = "프로필 url")
    String profileUrl,

    @JsonProperty("isOpened")
    @Schema(description = "개봉 여부")
    Boolean isOpened
) {

}