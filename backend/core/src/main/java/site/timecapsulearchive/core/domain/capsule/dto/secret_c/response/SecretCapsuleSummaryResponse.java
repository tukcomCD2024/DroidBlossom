package site.timecapsulearchive.core.domain.capsule.dto.secret_c.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import lombok.Builder;

@Schema(description = "비밀 캡슐 요약 정보")
@Builder
public record SecretCapsuleSummaryResponse(

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "생성자 프로필 url")
    String profileUrl,

    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "제목")
    String title,

    @Schema(description = "개봉일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 생성 주소")
    String address,

    @Schema(description = "캡슐 생성 도로 이름")
    String roadName,

    @Schema(description = "개봉 여부")
    Boolean isOpened,

    @Schema(description = "캡슐 생성 일")
    ZonedDateTime createdAt
) {

}
