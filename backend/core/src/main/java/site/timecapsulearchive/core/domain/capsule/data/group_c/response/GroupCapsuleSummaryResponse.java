package site.timecapsulearchive.core.domain.capsule.data.group_c.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;

@Schema(description = "그룹 캡슐 요약 정보")
public record GroupCapsuleSummaryResponse(

    @Schema(description = "캡슐 아이디")
    Long id,

    @Schema(description = "생성자 닉네임")
    String nickname,

    @Schema(description = "캡슐 스킨 url")
    String skinUrl,

    @Schema(description = "제목")
    String title,

    @Schema(description = "생성일")
    ZonedDateTime dueDate,

    @Schema(description = "캡슐 생성 주소")
    String address
) {

}