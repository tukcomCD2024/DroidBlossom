package site.timecapsulearchive.core.domain.member.data.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이메일 중복 여부 응답")
public record CheckEmailDuplicationResponse(

    @Schema(description = "이메일 중복 여부")
    Boolean isDuplicated
) {

}
