package site.timecapsulearchive.core.domain.capsule.dto.secret_c.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;

@Schema(description = "비밀 캡슐 요약 정보")
public record SecretCapsuleSummaryResponse(

    @Schema(description = "캡슐 아이디")
    Long id,

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "제목")
    String title,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "개봉 여부")
    Boolean isOpened,

    @Schema(description = "생성 시간")
    ZonedDateTime createdAt
) {

}
