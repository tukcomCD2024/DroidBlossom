package site.timecapsulearchive.core.domain.capsule.dto.public_c.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import org.springframework.validation.annotation.Validated;

@Schema(description = "공개 캡슐 요약 정보")
@Validated
public record PublicCapsuleSummaryResponse(
    @JsonProperty("id")
    @Schema(description = "캡슐 아이디")
    Long id,

    @JsonProperty("nickname")
    @Schema(description = "생성자 닉네임")
    String nickname,

    @JsonProperty("skinUrl")
    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @JsonProperty("title")
    @Schema(description = "제목")
    String title,

    @JsonProperty("dueDate")
    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @JsonProperty("address")
    @Schema(description = "캡슐 생성 주소")
    String address
) {

}
